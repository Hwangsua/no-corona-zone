(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (global = global || self, factory(global.svgParser = {}));
}(this, (function (exports) { 'use strict';

    function getLocator(source, options) {
        if (options === void 0) { options = {}; }
        var offsetLine = options.offsetLine || 0;
        var offsetColumn = options.offsetColumn || 0;
        var originalLines = source.split('\n');
        var start = 0;
        var lineRanges = originalLines.map(function (line, i) {
            var end = start + line.length + 1;
            var range = { start: start, end: end, line: i };
            start = end;
            return range;
        });
        var i = 0;
        function rangeContains(range, index) {
            return range.start <= index && index < range.end;
        }
        function getLocation(range, index) {
            return { line: offsetLine + range.line, column: offsetColumn + index - range.start, character: index };
        }
        function locate(search, startIndex) {
            if (typeof search === 'string') {
                search = source.indexOf(search, startIndex || 0);
            }
            var range = lineRanges[i];
            var d = search >= range.end ? 1 : -1;
            while (range) {
                if (rangeContains(range, search))
                    return getLocation(range, search);
                i += d;
                range = lineRanges[i];
            }
        }
        return locate;
    }
    function locate(source, search, options) {
        if (typeof options === 'number') {
            throw new Error('locate takes a { startIndex, offsetLine, offsetColumn } object as the third argument');
        }
        return getLocator(source, options)(search, options && options.startIndex);
    }

    var validNameCharacters = /[a-zA-Z0-9:_-]/;
    var whitespace = /[\s\t\r\n]/;
    var quotemark = /['"]/;

    function repeat(str, i) {
        var result = '';
        while (i--) { result += str; }
        return result;
    }

    function parse(source) {
        var header = '';
        var stack = [];

        var state = metadata;
        var currentElement = null;
        var root = null;

        function error(message) {
            var ref = locate(source, i);
            var line = ref.line;
            var column = ref.column;
            var before = source.slice(0, i);
            var beforeLine = /(^|\n).*$/.exec(before)[0].replace(/\t/g, '  ');
            var after = source.slice(i);
            var afterLine = /.*(\n|$)/.exec(after)[0];

            var snippet = "" + beforeLine + afterLine + "\n" + (repeat(' ', beforeLine.length)) + "^";

            throw new Error(
                (message + " (" + line + ":" + column + "). If this is valid SVG, it's probably a bug in svg-parser. Please raise an issue at https://github.com/Rich-Harris/svg-parser/issues – thanks!\n\n" + snippet)
            );
        }

        function metadata() {
            while ((i < source.length && source[i] !== '<') || !validNameCharacters.test(source[i + 1])) {
                header += source[i++];
            }

            return neutral();
        }

        function neutral() {
            var text = '';
            while (i < source.length && source[i] !== '<') { text += source[i++]; }

            if (/\S/.test(text)) {
                currentElement.children.push({ type: 'text', value: text });
            }

            if (source[i] === '<') {
                return tag;
            }

            return neutral;
        }

        function tag() {
            var char = source[i];

            if (char === '?') { return neutral; } // <?xml...

            if (char === '!') {
                if (source.slice(i + 1, i + 3) === '--') { return comment; }
                if (source.slice(i + 1, i + 8) === '[CDATA[') { return cdata; }
                if (/doctype/i.test(source.slice(i + 1, i + 8))) { return neutral; }
            }

            if (char === '/') { return closingTag; }

            var tagName = getName();

            var element = {
                type: 'element',
                tagName: tagName,
                properties: {},
                children: []
            };

            if (currentElement) {
                currentElement.children.push(element);
            } else {
                root = element;
            }

            var attribute;
            while (i < source.length && (attribute = getAttribute())) {
                element.properties[attribute.name] = attribute.value;
            }

            var selfClosing = false;

            if (source[i] === '/') {
                i += 1;
                selfClosing = true;
            }

            if (source[i] !== '>') {
                error('Expected >');
            }

            if (!selfClosing) {
                currentElement = element;
                stack.push(element);
            }

            return neutral;
        }

        function comment() {
            var index = source.indexOf('-->', i);
            if (!~index) { error('expected -->'); }

            i = index + 2;
            return neutral;
        }

        function cdata() {
            var index = source.indexOf(']]>', i);
            if (!~index) { error('expected ]]>'); }

            currentElement.children.push(source.slice(i + 7, index));

            i = index + 2;
            return neutral;
        }

        function closingTag() {
            var tagName = getName();

            if (!tagName) { error('Expected tag name'); }

            if (tagName !== currentElement.tagName) {
                error(("Expected closing tag </" + tagName + "> to match opening tag <" + (currentElement.tagName) + ">"));
            }

            allowSpaces();

            if (source[i] !== '>') {
                error('Expected >');
            }

            stack.pop();
            currentElement = stack[stack.length - 1];

            return neutral;
        }

        function getName() {
            var name = '';
            while (i < source.length && validNameCharacters.test(source[i])) { name += source[i++]; }

            return name;
        }

        function getAttribute() {
            if (!whitespace.test(source[i])) { return null; }
            allowSpaces();

            var name = getName();
            if (!name) { return null; }

            var value = true;

            allowSpaces();
            if (source[i] === '=') {
                i += 1;
                allowSpaces();

                value = getAttributeValue();
                if (!isNaN(value) && value.trim() !== '') { value = +value; } // TODO whitelist numeric attributes?
            }

            return { name: name, value: value };
        }

        function getAttributeValue() {
            return quotemark.test(source[i]) ? getQuotedAttributeValue() : getUnquotedAttributeValue();
        }

        function getUnquotedAttributeValue() {
            var value = '';
            do {
                var char = source[i];
                if (char === ' ' || char === '>' || char === '/') {
                    return value;
                }

                value += char;
                i += 1;
            } while (i < source.length);

            return value;
        }

        function getQuotedAttributeValue() {
            var quotemark = source[i++];

            var value = '';
            var escaped = false;

            while (i < source.length) {
                var char = source[i++];
                if (char === quotemark && !escaped) {
                    return value;
                }

                if (char === '\\' && !escaped) {
                    escaped = true;
                }

                value += escaped ? ("\\" + char) : char;
                escaped = false;
            }
        }

        function allowSpaces() {
            while (i < source.length && whitespace.test(source[i])) { i += 1; }
        }

        var i = metadata.length;
        while (i < source.length) {
            if (!state) { error('Unexpected character'); }
            state = state();
            i += 1;
        }

        if (state !== neutral) {
            error('Unexpected end of input');
        }

        if (root.tagName === 'svg') { root.metadata = header; }
        return {
            type: 'root',
            children: [root]
        };
    }

    exports.parse = parse;

    Object.defineProperty(exports, '__esModule', { value: true });

})));
//# sourceMappingURL=svg-parser.umd.js.map

const parsed = parse( `
    <svg version="1.0" xmlns="http://www.w3.org/2000/svg"
 width="2938.000000pt" height="2560.000000pt" viewBox="0 0 2938.000000 2560.000000"
 preserveAspectRatio="xMidYMid meet">
        <g transform="translate(0.000000,2560.000000) scale(0.100000,-0.100000)"
fill="#000000" stroke="none">
<path d="M4920 14469 c-102 -21 -185 -38 -186 -39 -1 0 -100 -142 -220 -315
-206 -297 -220 -315 -249 -315 -16 0 -250 22 -520 49 -395 40 -495 47 -515 38
-14 -7 -137 -154 -273 -327 -349 -442 -337 -426 -337 -467 0 -19 50 -205 112
-413 l111 -378 -131 -204 c-141 -217 -154 -250 -114 -297 25 -31 499 -146 538
-131 15 6 163 75 328 154 l301 143 293 -13 293 -13 245 -211 c205 -175 251
-210 276 -210 37 0 425 48 450 56 10 3 97 91 195 195 l177 189 70 22 c646 196
699 214 717 238 21 30 185 1053 175 1093 -4 18 -53 60 -158 138 l-153 113
-435 75 -435 76 -5 180 -5 180 -114 195 c-62 107 -123 205 -134 218 -28 31
-73 28 -297 -19z m-480 -1369 c0 -29 1 -30 50 -30 l50 0 0 -35 0 -35 -50 0
c-33 0 -50 -4 -50 -12 0 -28 40 -76 79 -94 l41 -19 -20 -32 c-18 -30 -21 -31
-50 -22 -16 6 -44 24 -61 40 l-31 29 -25 -31 c-25 -29 -71 -59 -92 -59 -10 0
-46 59 -39 65 2 1 18 10 36 19 35 18 72 69 72 99 0 14 -9 17 -45 17 -45 0 -45
0 -45 35 l0 35 50 0 c49 0 50 1 50 30 0 28 2 30 40 30 38 0 40 -2 40 -30z
m240 -135 l0 -165 -40 0 -40 0 0 55 0 55 -40 0 c-39 0 -40 1 -40 35 0 34 1 35
40 35 l40 0 0 75 0 75 40 0 40 0 0 -165z m1020 -110 l0 -275 -40 0 -40 0 0
275 0 275 40 0 40 0 0 -275z m-510 210 l0 -35 -70 0 c-39 0 -70 -4 -70 -10 0
-23 59 -71 107 -88 l51 -17 -14 -33 c-13 -31 -16 -33 -46 -27 -47 10 -99 39
-127 69 l-25 26 -30 -29 c-36 -33 -78 -56 -124 -66 -30 -6 -33 -4 -46 27 l-14
33 51 18 c52 18 107 61 107 84 0 9 -19 13 -70 13 l-70 0 0 35 0 35 195 0 195
0 0 -35z m292 -62 c4 -106 31 -171 89 -212 l38 -28 -22 -31 c-12 -18 -26 -32
-30 -32 -21 0 -69 45 -94 87 l-26 45 -20 -38 c-23 -46 -42 -69 -79 -95 l-27
-20 -25 38 -25 37 28 17 c62 37 99 120 108 242 l6 77 37 0 38 0 4 -87z m-252
-208 l0 -35 -100 0 -100 0 0 -95 0 -95 -40 0 -40 0 0 95 0 95 -95 0 -95 0 0
35 0 35 235 0 235 0 0 -35z m-614 -24 c54 -24 73 -71 51 -124 -19 -45 -74 -67
-167 -67 -93 0 -148 22 -167 67 -11 28 -12 42 -2 70 16 49 70 73 169 73 52 0
88 -6 116 -19z"/>
<path d="M4446 12720 c-58 -18 -55 -54 6 -71 71 -19 156 9 142 46 -6 15 -65
35 -99 34 -11 0 -33 -5 -49 -9z"/>
</g>
    </svg>
` );

/*
{
  type: 'root',
  children: [
    {
      type: 'element',
      tagName: 'svg',
      properties: {
        viewBox: '0 0 100 100'
      },
      children: [...]
    }
  ]
}
*/