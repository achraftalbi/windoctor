package com.winbit.windoctor.service;

import com.winbit.windoctor.domain.Authority;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.AuthorityRepository;
import com.winbit.windoctor.repository.PersistentTokenRepository;
import com.winbit.windoctor.repository.StructureRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.UserSearchRepository;
import com.winbit.windoctor.security.AuthoritiesConstants;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.service.util.RandomUtil;
import com.winbit.windoctor.web.rest.dto.PatientDTO;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private StructureRepository structureRepository;

    @Inject
    private SessionService sessionService;

    @Inject
    private MailService mailService;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                userSearchRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
        return Optional.empty();
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findOneByResetKey(key)
            .filter(user -> {
                DateTime oneDayAgo = DateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo.toInstant().getMillis());
            })
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailAndStructure(mail, structureRepository.findOneById(SecurityUtils.getCurrerntStructure()))
            .filter(user -> user.getActivated() == true)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(DateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_DOCTOR");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createPatientInformation(PatientDTO patientCreated) {

        User patient = new User();
        Authority authority = authorityRepository.findOne("ROLE_PATIENT");
        Set<Authority> authorities = new HashSet<>();
        BigDecimal patientsNumber = userRepository.findPatientsNumber("ROLE_PATIENT", SecurityUtils.getCurrerntStructure());
        log.info("patientsNumber " + patientsNumber);
        // new user gets initially a generated password
        String loginToUse;
        if (patientCreated.getLogin() == null) {
            loginToUse = (patientCreated.getFirstName() + patientCreated.getLastName()).
                replaceAll("\\s+", "").replaceAll("\\t+", "").toLowerCase();
        } else {
            loginToUse = patientCreated.getLogin();
        }
        for (int i = 1; ; i++) {
            User user = userRepository.findByLogin(loginToUse);
            if (user != null) {
                loginToUse = loginToUse + i;
                continue;
            } else {
                break;
            }
        }
        patient.setLogin(loginToUse);
        if (patientCreated.getPassword() == null) {
            patient.setPassword(patient.getLogin());
        } else {
            patient.setPassword(patientCreated.getPassword());
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));

        patient.setFirstName(patientCreated.getFirstName());
        patient.setLastName(patientCreated.getLastName());
        patient.setBirthDate(patientCreated.getBirthDate());
        patient.setDiseases(patientCreated.getDiseases());
        patient.setAllergies(patientCreated.getAllergies());
        patient.setMutualAssurance(patientCreated.getMutualAssurance());
        patient.setProfession(patientCreated.getProfession());
        patient.setFacebook(patientCreated.getFacebook());
        patient.setNumber(new BigDecimal(patientsNumber == null ? 0 + 1 : patientsNumber.intValue() + 1));

        patient.setInitial_balance(new BigDecimal(0l));
        patient.setPhoneNumber(patientCreated.getPhoneNumber() == null ? patientCreated.getPhoneNumber()
            : patientCreated.getPhoneNumber().substring(5, patientCreated.getPhoneNumber().length()));
        patient.setPhoneNumber2(patientCreated.getPhoneNumber2() == null || patientCreated.getPhoneNumber2().length()==0 ? patientCreated.getPhoneNumber2()
            : patientCreated.getPhoneNumber2().substring(5, patientCreated.getPhoneNumber2().length()));
        patient.setPhoneNumber3(patientCreated.getPhoneNumber3() == null || patientCreated.getPhoneNumber3().length()==0 ? patientCreated.getPhoneNumber3()
            : patientCreated.getPhoneNumber3().substring(5, patientCreated.getPhoneNumber3().length()));
        patient.setRemark(patientCreated.getRemark());
        patient.setConsultationReason(patientCreated.getConsultationReason());
        patient.setEmail(patientCreated.getEmail());
        patient.setLangKey(patientCreated.getLangKey());
        Long structureId = sessionService.getCurrentStructure();
        if (structureId != null) {
            patient.setStructure(structureRepository.findOneById(structureId));
        }
        // new user is not active
        if (patientCreated.getActivated() == null || patientCreated.getActivated() == false) {
            patient.setActivated(false);
        } else {
            patient.setActivated(true);
        }

        if (patientCreated.getBlocked() == null || patientCreated.getBlocked() == false) {
            patient.setBlocked(false);
        } else {
            patient.setBlocked(true);
        }

        if (patientCreated.getSmoking() == null || patientCreated.getSmoking() == false) {
            patient.setSmoking(false);
        } else {
            patient.setSmoking(true);
        }

        if (patientCreated.getBleedingWhileBrushing() == null || patientCreated.getBleedingWhileBrushing() == false) {
            patient.setBleedingWhileBrushing(false);
        } else {
            patient.setBleedingWhileBrushing(true);
        }

        if (patientCreated.getToothSensitivity() == null || patientCreated.getToothSensitivity() == false) {
            patient.setToothSensitivity(false);
        } else {
            patient.setToothSensitivity(true);
        }
        patient.setReferralBy(patientCreated.getReferralBy());
        patient.setDoctorTreating(patientCreated.getDoctorTreating());
        patient.setAddress(patientCreated.getAddress());
        patient.setWorkingHours(patientCreated.getWorkingHours());

        authorities.add(authority);
        patient.setAuthorities(authorities);
        patient.setPicture(patientCreated.getPicture());
        log.info("patient.getLogin() " + patient.getLogin());
        patient = userRepository.save(patient);
        patientCreated.setId(patient.getId());
        log.info("patientCreated.getId() " + patientCreated.getId());
        userSearchRepository.save(patient);
        log.debug("Created Information for Patient: {}", patient);
        // send mail
        //This service was down hold now, and will come back after. We want to permit the client to choose
        // Between sending or not.
        //mailService.sendPatientCreationAccountEmail(patient, patient.getStructure(), patientCreated.getPassword());
        return patient;
    }

    public User createDoctorInformation(String login, String password, String firstName, String lastName, String email,
                                        String langKey, Boolean blocked, Boolean activated, byte[] picture, Long structureId) {

        User doctor = new User();
        Authority authority = authorityRepository.findOne("ROLE_DOCTOR");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        doctor.setLogin(login);
        // new user gets initially a generated password
        doctor.setPassword(encryptedPassword);
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setEmail(email);
        doctor.setLangKey(langKey);
        if (structureId != null) {
            doctor.setStructure(structureRepository.findOneById(structureId));
        }
        // new user is not active
        if (activated == null || activated == false) {
            doctor.setActivated(false);
        } else {
            doctor.setActivated(true);
        }

        if (blocked == null || blocked == false) {
            doctor.setBlocked(false);
        } else {
            doctor.setBlocked(true);
        }

        authorities.add(authority);
        doctor.setAuthorities(authorities);
        doctor.setPicture(picture);
        userRepository.save(doctor);
        userSearchRepository.save(doctor);
        log.debug("Created Information for Doctor: {}", doctor);
        return doctor;
    }

    public User updatePatientInformation(PatientDTO patientUpdated) {

        Optional<User> rst = userRepository.findOneById(patientUpdated.getId());
        User patient = rst.get();
        String loginToUse;
        if (patientUpdated.getLogin() == null) {
            loginToUse = (patientUpdated.getFirstName() + patientUpdated.getLastName()).
                replaceAll("\\s+", "").replaceAll("\\t+", "").toLowerCase();
        } else {
            loginToUse = patientUpdated.getLogin();
        }
        if(patientUpdated.getLogin() == null){
            for (int i = 1; ; i++) {
                User user = userRepository.findByLogin(loginToUse);
                if (user != null) {
                    loginToUse = loginToUse + i;
                    continue;
                } else {
                    break;
                }
            }
        }

        patient.setLogin(loginToUse);
        if (patientUpdated.getPassword() != null
            && patientUpdated.getPassword().length() > 0) {
            String encryptedPassword = passwordEncoder.encode(patientUpdated.getPassword());
            // new user updated its password
            patient.setPassword(encryptedPassword);
        } else {
            patient.setPassword(patient.getPassword());
        }
        patient.setFirstName(patientUpdated.getFirstName());
        patient.setLastName(patientUpdated.getLastName());
        patient.setBirthDate(patientUpdated.getBirthDate());
        patient.setDiseases(patientUpdated.getDiseases());
        patient.setAllergies(patientUpdated.getAllergies());
        patient.setMutualAssurance(patientUpdated.getMutualAssurance());
        patient.setProfession(patientUpdated.getProfession());
        patient.setFacebook(patientUpdated.getFacebook());
        patient.setPhoneNumber(patientUpdated.getPhoneNumber() == null ? patientUpdated.getPhoneNumber()
            : patientUpdated.getPhoneNumber().substring(5, patientUpdated.getPhoneNumber().length()));
        patient.setPhoneNumber2(patientUpdated.getPhoneNumber2() == null  || patientUpdated.getPhoneNumber2().length()==0? patientUpdated.getPhoneNumber2()
            : patientUpdated.getPhoneNumber2().substring(5, patientUpdated.getPhoneNumber2().length()));
        patient.setPhoneNumber3(patientUpdated.getPhoneNumber3() == null  || patientUpdated.getPhoneNumber3().length()==0? patientUpdated.getPhoneNumber3()
            : patientUpdated.getPhoneNumber3().substring(5, patientUpdated.getPhoneNumber3().length()));
        patient.setRemark(patientUpdated.getRemark());
        patient.setConsultationReason(patientUpdated.getConsultationReason());
        patient.setEmail(patientUpdated.getEmail());
        patient.setLangKey(patientUpdated.getLangKey());
        // new user is not active
        if (patientUpdated.getActivated() == null || patientUpdated.getActivated() == false) {
            patient.setActivated(false);
        } else {
            patient.setActivated(true);
        }

        if (patientUpdated.getBlocked() == null || patientUpdated.getBlocked() == false) {
            patient.setBlocked(false);
        } else {
            patient.setBlocked(true);
        }
        if (patientUpdated.getSmoking() == null || patientUpdated.getSmoking() == false) {
            patient.setSmoking(false);
        } else {
            patient.setSmoking(true);
        }

        if (patientUpdated.getBleedingWhileBrushing() == null || patientUpdated.getBleedingWhileBrushing() == false) {
            patient.setBleedingWhileBrushing(false);
        } else {
            patient.setBleedingWhileBrushing(true);
        }

        if (patientUpdated.getToothSensitivity() == null || patientUpdated.getToothSensitivity() == false) {
            patient.setToothSensitivity(false);
        } else {
            patient.setToothSensitivity(true);
        }
        patient.setReferralBy(patientUpdated.getReferralBy());
        patient.setDoctorTreating(patientUpdated.getDoctorTreating());
        patient.setAddress(patientUpdated.getAddress());
        patient.setWorkingHours(patientUpdated.getWorkingHours());
        // new user gets registration key
        patient.setPicture(patientUpdated.getPicture());
        userRepository.save(patient);
        userSearchRepository.save(patient);
        log.debug("Update Information for Patient: {}", patient);
        return patient;
    }

    public User updateDoctorInformation(String login, String password, String firstName, String lastName, String email,
                                        String langKey, Boolean blocked, Boolean activated, byte[] picture, Long structureId) {

        Optional<User> rst = userRepository.findOneByLogin(login);
        User doctor = rst.get();
        String encryptedPassword = passwordEncoder.encode(password);
        // new user gets initially a generated password
        doctor.setPassword(encryptedPassword);
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setEmail(email);
        doctor.setLangKey(langKey);
        // new user is not active
        if (activated == null || activated == false) {
            doctor.setActivated(false);
        } else {
            doctor.setActivated(true);
        }

        if (blocked == null || blocked == false) {
            doctor.setBlocked(false);
        } else {
            doctor.setBlocked(true);
        }

        if (structureId != null) {
            doctor.setStructure(structureRepository.findOneById(structureId));
        }

        // new user gets registration key
        doctor.setPicture(picture);
        userRepository.save(doctor);
        userSearchRepository.save(doctor);
        log.debug("Update Information for Doctor: {}", doctor);
        return doctor;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            userRepository.save(u);
            userSearchRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        currentUser.getAuthorities().size(); // eagerly load the association
        return currentUser;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = new LocalDate();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     *
     @Scheduled(cron = "0 0 1 * * ?")
     public void removeNotActivatedUsers() {
     DateTime now = new DateTime();
     List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
     for (User user : users) {
     log.debug("Deleting not activated user {}", user.getLogin());
     userRepository.delete(user);
     userSearchRepository.delete(user);
     }
     }*/

    /**
     * Find all patient of the current structure
     *
     * @param pageable
     * @return patients list
     */
    public Page<User> findAllPatients(Pageable pageable) {
        return getUsersByRole(AuthoritiesConstants.PATIENT, pageable);
    }

    public Page<User> findAllDoctors(Pageable pageable) {
        return getUsersByRole(AuthoritiesConstants.DOCTOR, pageable);
    }

    private Page<User> getUsersByRole(String role, Pageable pageable) {
        Long structureId = sessionService.getCurrentStructure();
        if (structureId != null) {
            // role case : get only doctors of the current structure
            return userRepository.findAll(role, structureId, pageable);
        } else {
            // admin case : get all doctors
            return userRepository.findAll(role, pageable);
        }
    }


}
