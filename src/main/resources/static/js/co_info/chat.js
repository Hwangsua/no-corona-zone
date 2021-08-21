'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

function connect(event) {
    console.log("왜 연결 안되지");
    username = document.querySelector('#name').value.trim();

    if(username) {
        console.log("연결좀 되라");
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        console.log("socket : " + socket);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
    document.querySelector(".chat").style.display='display';
}


function onConnected() {
    console.log("넌 도대체 언제 실행되니")
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
    console.log("실행될 생각은 있고?")
}


function onError(error) {
    console.log("넌 좀 그만 나왔으면 ")
    connectingElement.textContent = '연결실패!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    console.log("보내져라 메세지메세지!!")
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    console.log("메세지좀 받아봐...")
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' 님이 들어왔습니다!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' 님이 나갔습니다!';
    } else {
        messageElement.classList.add('chat-message');

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)