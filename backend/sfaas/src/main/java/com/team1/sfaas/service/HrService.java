package com.team1.sfaas.service;

import com.team1.sfaas.mapper.HrMapper;
import com.team1.sfaas.model.Hr;
import com.team1.sfaas.security.HashUtil;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class HrService {

    private final HrMapper hrMapper;

    @Autowired
    public HrService(HrMapper hrMapper) {
        this.hrMapper = hrMapper;
    }

    public Hr getUserById(String id) {
        return hrMapper.findById(id);
    }

    public Hr getUserByName(String id, String pw){
        return hrMapper.findByName(id,pw);
    }

    public List<Hr> getAllUsers() {
        return hrMapper.findAll();
    }

    public boolean isUserExist(String id) {
        return hrMapper.countById(id) > 0 ? true : false;
    }

    public boolean isUserExistEmail(String email) {
        return hrMapper.countByEmail(email) > 0 ? true : false;
    }

    public boolean resetPasswordByEmail(String email, String newPassword) {
        //  DB에 업데이트
        int updatedRows = hrMapper.updatePassword(email,newPassword);
        return updatedRows > 0; // 1 이상이면 성공
    }

    public int createUser(Hr hr) {
        return hrMapper.insert(hr);
    }

    public int updateUser(Hr hr) {
        return hrMapper.update(hr);
    }

    public int deleteUser(String id) {
        return hrMapper.delete(id);
    }
}
