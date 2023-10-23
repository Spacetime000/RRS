package com.space000.service;

import com.space000.constant.Role;
import com.space000.dto.MyInfoDto;
import com.space000.dto.RegisterDto;
import com.space000.entity.Member;
import com.space000.entity.MemberImg;
import com.space000.repository.MemberImgRepository;
import com.space000.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    @Value("${location}")
    private String location;

    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    //회원가입
    public void saveMember(RegisterDto registerDto) {
        LocalDate birth = LocalDate.of(registerDto.getYear(), registerDto.getMonth(), registerDto.getDay());
        Member member = modelMapper.map(registerDto, Member.class);
        MemberImg memberImg = MemberImg.createMemberImg();
        member.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        member.setBirth(birth);
        member.setRole(Role.USER);
        member.setMemberImg(memberImg);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MyInfoDto getMyinfo(String email) {
        Member member = memberRepository.findByEmail(email);
        MyInfoDto myInfoDto = new MyInfoDto();
        myInfoDto.setId(member.getMemberImg().getId());
        myInfoDto.setNickname(member.getNickname());
        myInfoDto.setFileUrl(member.getMemberImg().getFileUrl());
        return myInfoDto;
    }

    public void updateMyinfo(Long id, MultipartFile multipartFile) throws Exception {
        MemberImg memberImg = memberImgRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (!memberImg.getFileUrl().equals("/img/person-circle.svg")) {
            String delUrl = fileService.replaceUrl(memberImg.getFileUrl());
            fileService.deleteFile(delUrl);
        }

        String oriFileName = multipartFile.getOriginalFilename();
        String fileName = fileService.uploadFile(location + "/member", oriFileName, multipartFile.getBytes());
        String fileUrl = "/files/member/" + fileName;
        memberImg.setFileName(fileName);
        memberImg.setOriFileName(oriFileName);
        memberImg.setFileUrl(fileUrl);

    }

    /**
     * 중복 검사
     * @param target 검사 대상
     * @param key 검사 대상의 분류(email, nickname)
     */
    public void validateDuplicate(String target, String key) throws IllegalStateException, IllegalArgumentException {
        Member member;

        switch (key) {
            case "email":
                member = memberRepository.findByEmail(target);
                if (member != null) {
                    log.info("이메일 중복 체크 예외");
                    throw new IllegalStateException("이메일 예외");
                }

                break;

            case "nickname":
                member = memberRepository.findByNickname(target);
                if (member != null) {
                    log.info("닉네임 중복 체크 예외");
                    throw new IllegalStateException("닉네임 예외");
                }
                break;

            default:
                log.info("잘못된 key값");
                throw new IllegalArgumentException("잘못된 key");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 회원");
        }

        log.info("정상 로그인");

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
