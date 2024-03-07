package com.ganpatispringsecurity.springsecurity.service.serviceImpl;

import com.ganpatispringsecurity.springsecurity.dto.UserDto;
import com.ganpatispringsecurity.springsecurity.entity.Role;
import com.ganpatispringsecurity.springsecurity.entity.User;
import com.ganpatispringsecurity.springsecurity.repository.RoleRepository;
import com.ganpatispringsecurity.springsecurity.repository.UserRepository;
import com.ganpatispringsecurity.springsecurity.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${normal.role.id}")
    String normalRoleId;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto addUser(UserDto userDto) {

        //Encoding Password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = modelMapper.map(userDto, User.class);
        Role role = roleRepository.findById(normalRoleId).get();
        user.getRoles().add(role);
        User savedUser = userRepo.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUser() {
        return userRepo.findAll().stream().map(user -> modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = userRepo.findById(userId).get();

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUserById(long userId, UserDto userDto) {
        User user = userRepo.findById(userId).get();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        User updatedUser = userRepo.save(user);
        modelMapper.map(updatedUser,UserDto.class);
        return modelMapper.map(updatedUser,UserDto.class);
    }

    @Override
    public String deleteUserById(long userId) {
        userRepo.deleteById(userId);
        return "User deleted Successfully";
    }
}
