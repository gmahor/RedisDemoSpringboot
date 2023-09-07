package com.redisdemo.repositories;

import com.redisdemo.entities.RedisUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomSortingAndPagination extends PagingAndSortingRepository<RedisUser,Long> {
}
