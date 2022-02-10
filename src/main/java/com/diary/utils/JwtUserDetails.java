package com.diary.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.diary.model.UserDao;

@Service
public class JwtUserDetails implements UserDetailsService {

	@Autowired
	UtilsService utilsService;
	
	public List<GrantedAuthority> getAuthorities(List<String> rolesFromDb){
		List<GrantedAuthority> roles = new ArrayList<>();
		for(String role :rolesFromDb) {
			roles.add(new SimpleGrantedAuthority(role));
		}
		return roles;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDao userDao = utilsService.checkUserExist(username);
		if(userDao == null) {
			throw new UsernameNotFoundException("User not found with" + username);
		}
		return new User(userDao.getUserName(), userDao.getPassword(), getAuthorities(userDao.getRoles()));
	}

}
