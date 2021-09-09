package com.megait.nocoronazone.service;


import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.ProfileImage;
import com.megait.nocoronazone.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImageService {
    private final ProfileImageRepository profileImageRepository;

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path uploadPath = Paths.get(uploadDir);

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath , StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Transactional
    public ProfileImage saveProfileImage(Member member, final ProfileImage memberImage, MultipartFile multipartFile) throws IOException {

        ProfileImage profileImage = profileImageRepository.findByMember(member).orElseGet(()-> memberImage);
        String uploadDir = "memberImage/";
        String originalFilename = multipartFile.getOriginalFilename();
        String filename = String.valueOf(member.getNo());
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String imageName = StringUtils.cleanPath(filename + extension);
        profileImage.setFilename("/" + uploadDir + imageName);
        profileImage.setMember(member);

        saveFile(uploadDir, imageName, multipartFile);
        return profileImageRepository.save(profileImage);
    }
}

