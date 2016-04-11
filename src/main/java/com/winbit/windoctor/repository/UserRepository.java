package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.domain.User;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(DateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    //@Query("select u from User where u.email= ?1 and u.structure.id = ?2")
    Optional<User> findOneByEmailAndStructure(String email, Structure structure);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long id);

    @Query("select u from User u join u.authorities a join u.structure s where a.name= ?1 and s.id = ?2 order by u.firstName, u.lastName asc")
    Page<User> findAll(String role, Long id, Pageable var1);

    @Query("select u from User u join u.authorities a where a.name= ?1")
    Page<User> findAll(String role, Pageable var1);

    @Query("select u from User u join u.authorities a where a.name= ?2 and u.structure.id = ?3 and" +
        " ( lower(u.login) like lower(?1) or lower(u.firstName) like lower(?1) or lower(u.lastName) like lower(?1) or lower(u.email) like lower(?1)) ")
    Page<User> findAllMatchString(String query, String role, Long structureId, Pageable var1);


    @Override
    void delete(User t);

}
