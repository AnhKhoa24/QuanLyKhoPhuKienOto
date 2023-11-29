package com.anhkhoa.WebNT.service;

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

import com.anhkhoa.WebNT.mapper.accountMapper;
import com.anhkhoa.WebNT.model.account;
import com.anhkhoa.WebNT.model.accountExample;

@Service
public class CustomerUserDetailService implements UserDetailsService{

	@Autowired
	accountMapper accMapper;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		accountExample ex = new accountExample();
		ex.createCriteria().andNameEqualTo(username);
		List<account> list = accMapper.selectByExample(ex);
		
		if(list.size() > 0) {
			account ac = list.get(0);
			
			List<GrantedAuthority> granList = new ArrayList<GrantedAuthority>();
			GrantedAuthority auth = new SimpleGrantedAuthority(ac.getRole());
			granList.add(auth);
			UserDetails userDetails = new User(username, ac.getPassword(), granList);
			return userDetails;
		}else {
			new UsernameNotFoundException("Login fail!");
		}
		return null;
	}
	
	
	
	
	

	
}
