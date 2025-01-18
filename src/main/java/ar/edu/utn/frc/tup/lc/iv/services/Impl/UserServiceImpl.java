package ar.edu.utn.frc.tup.lc.iv.services.Impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.user.UserDto;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.UserRepository;
import ar.edu.utn.frc.tup.lc.iv.services.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.lang.reflect.Type;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<UserDto> allUsers() {
        List<UserEntity>users=this.userRepository.findAll();
        if (!users.isEmpty()){
            Type listType = new TypeToken<List<UserDto>>(){}.getType();
            return modelMapper.map(users,listType);
        }
        throw  new RuntimeException("No hay users");
    }
}
