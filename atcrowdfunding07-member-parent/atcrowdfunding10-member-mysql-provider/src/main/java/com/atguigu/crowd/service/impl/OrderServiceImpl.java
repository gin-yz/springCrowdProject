package com.atguigu.crowd.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.atguigu.crowd.entity.po.OrderPO;
import com.atguigu.crowd.entity.po.OrderProjectPO;
import com.atguigu.crowd.entity.vo.OrderVO;
import com.atguigu.crowd.mapper.AddressDao;
import com.atguigu.crowd.mapper.OrderDao;
import com.atguigu.crowd.mapper.OrderProjectDao;
import com.atguigu.crowd.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crowd.entity.po.AddressPO;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;


@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderProjectDao orderProjectDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private AddressDao addressDao;

	@Override
	public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
		return orderProjectDao.selectOrderProjectVO(returnId);
	}

	@Override
	public List<AddressVO> getAddressVOList(Integer memberId) {
		AddressPO addressPOCondition = new AddressPO();
		addressPOCondition.setMemberId(memberId);

		List<AddressPO> addressPOList = addressDao.queryAll(addressPOCondition);
		
		List<AddressVO> addressVOList = new ArrayList<AddressVO>();
		
		for (AddressPO addressPO : addressPOList) {
			AddressVO addressVO = new AddressVO();
			BeanUtils.copyProperties(addressPO, addressVO);
			
			addressVOList.add(addressVO);
		}
		
		return addressVOList;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public void saveAddress(AddressVO addressVO) {
		
		AddressPO addressPO = new AddressPO();
		
		BeanUtils.copyProperties(addressVO, addressPO);
		
		addressDao.insert(addressPO);
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public void saveOrder(OrderVO orderVO) {

		OrderPO orderPO = new OrderPO();

		BeanUtils.copyProperties(orderVO, orderPO);

		OrderProjectPO orderProjectPO = new OrderProjectPO();

		BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProjectPO);

		// 保存orderPO自动生成的主键是orderProjectPO需要用到的外键
		orderDao.insert(orderPO);

		// 从orderPO中获取orderId
		Integer id = orderPO.getId();

		// 将orderId设置到orderProjectPO
		orderProjectPO.setOrderId(id);

		orderProjectDao.insert(orderProjectPO);
	}

}
