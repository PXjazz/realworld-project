package com.realworld.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.realworld.common.exception.BusinessException;
import com.realworld.common.util.JwtUtil;
import com.realworld.dto.request.LoginRequest;
import com.realworld.dto.request.RegisterRequest;
import com.realworld.dto.request.UpdateUserRequest;
import com.realworld.dto.response.UserResponse;
import com.realworld.entity.User;
import com.realworld.mapper.UserMapper;
import com.realworld.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        RegisterRequest.UserDto dto = request.getUser();

        User existing = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", dto.getEmail())
                        .or()
                        .eq("username", dto.getUsername())
        );
        if (existing != null) {
            String field = existing.getEmail().equals(dto.getEmail()) ? "email" : "username";
            throw new BusinessException(409, field + " has already been taken");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.insert(user);

        String token = jwtUtil.generateToken(user.getId());
        return buildUserResponse(user, token);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        LoginRequest.UserDto dto = request.getUser();

        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("email", dto.getEmail())
        );
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "email or password is invalid");
        }

        String token = jwtUtil.generateToken(user.getId());
        return buildUserResponse(user, token);
    }

    @Override
    public UserResponse getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(401, "Unauthorized");
        }
        String token = jwtUtil.generateToken(user.getId());
        return buildUserResponse(user, token);
    }

    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        UpdateUserRequest.UpdateUserDto dto = request.getUser();

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(401, "Unauthorized");
        }

        if (dto.getEmail() != null) {
            User existing = userMapper.selectOne(
                    new QueryWrapper<User>().eq("email", dto.getEmail()).ne("id", userId)
            );
            if (existing != null) {
                throw new BusinessException(409, "email has already been taken");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getUsername() != null) {
            User existing = userMapper.selectOne(
                    new QueryWrapper<User>().eq("username", dto.getUsername()).ne("id", userId)
            );
            if (existing != null) {
                throw new BusinessException(409, "username has already been taken");
            }
            user.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }
        if (dto.getImage() != null) {
            user.setImage(dto.getImage());
        }

        userMapper.updateById(user);

        String token = jwtUtil.generateToken(user.getId());
        return buildUserResponse(user, token);
    }

    private UserResponse buildUserResponse(User user, String token) {
        UserResponse.UserVO vo = new UserResponse.UserVO();
        vo.setEmail(user.getEmail());
        vo.setToken(token);
        vo.setUsername(user.getUsername());
        vo.setBio(user.getBio());
        vo.setImage(user.getImage());
        return new UserResponse(vo);
    }
}
