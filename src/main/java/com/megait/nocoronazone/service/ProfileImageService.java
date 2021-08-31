package com.megait.nocoronazone.service;


import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.ProfileImage;
import com.megait.nocoronazone.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//import com.megait.nocoronazone.form.ProfileImageForm;

@Service
@RequiredArgsConstructor
public class ProfileImageService {
    private final ProfileImageRepository profileImageRepository;
//
//    public ProfileImageService(ProfileImageRepository profileImageRepository) {
//        this.profileImageRepository = profileImageRepository;
//    }
//
//    @Transactional
//    public void saveProfileImage(Member member, ProfileImageForm profileImageForm) {
//
//        ProfileImage profileImage = ProfileImage.builder()
//                                    .member(member)
//                                    .fileName(profileImageForm.getFileName())
//                                    .filePath(profileImageForm.getFilePath())
//                                    .build();
//
//        profileImageRepository.save(profileImage);
//    }
//
//
//
//    @Transactional
//    public ProfileImage getProfileImage(Long no) {
//        Optional<ProfileImage> optionalProfileImage = profileImageRepository.findById(no);
//
//        ProfileImage profileImage = optionalProfileImage.get();
//        return profileImage;
//    }

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public ProfileImage saveProfileImage(Member member, ProfileImage memberImage, MultipartFile multipartFile) throws IOException {
        String imageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        memberImage.setFilename(imageName);
        memberImage.setMember(member);
        System.out.println(imageName);

        ProfileImage savedMemberImage = profileImageRepository.save(memberImage);
        System.out.println(savedMemberImage);

        String uploadDir = "/memberImage/" + member.getNo();
        System.out.println(uploadDir);

        saveFile(uploadDir, imageName, multipartFile);
        return savedMemberImage;
    }
}

