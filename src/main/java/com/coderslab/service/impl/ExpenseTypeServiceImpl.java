package com.coderslab.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderslab.entity.ExpenseType;
import com.coderslab.model.enums.RecordStatus;
import com.coderslab.repository.ExpenseTypeRepository;
import com.coderslab.service.ExpenseTypeService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 *
 */
@Slf4j
@Service
public class ExpenseTypeServiceImpl implements ExpenseTypeService {

	@Autowired private ExpenseTypeRepository expenseTypeRepository;
	@Autowired @PersistenceContext private EntityManager jpa;

	@Transactional
	@Override
	public ExpenseType save(ExpenseType obj) {
		return expenseTypeRepository.save(obj);
	}

	@Override
	public ExpenseType update(ExpenseType obj) {
		return save(obj);
	}

	@Override
	public List<ExpenseType> findAll() {
		return expenseTypeRepository.findAll();
	}

	@Override
	public ExpenseType findById(Long id) {
		return expenseTypeRepository.findById(id).orElse(null);
	}

	@Override
	public ExpenseType archiveById(Long id) {
		ExpenseType expenseType = findById(id);
		expenseType.setStatus(RecordStatus.D);
		return update(expenseType);
	}

	@Transactional
	@Override
	public Boolean deleteById(Long id) {
		try {
			expenseTypeRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	@Override
	public ExpenseType findByExpenseTypeName(String expenseTypeName) {
		return jpa.createQuery("SELECT e FROM ExpenseType e WHERE UPPER(e.expenseTypeName)=:etnm AND e.status=:stat", ExpenseType.class)
				.setParameter("etnm", expenseTypeName.toUpperCase())
				.setParameter("stat", RecordStatus.L)
				.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}

}
