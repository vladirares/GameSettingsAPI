package com.playtika.gamesettingsapi.unittests.servicesTests.userCRUDTests;

import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.services.factories.userCRUD.DefaultUserCRUD;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(MockitoJUnitRunner.class)
public class DefaultUserCrudTests {

    @InjectMocks
    DefaultUserCRUD defaultUserCrud;

    @Test
    public void hasIllegalRoleTest() {
        defaultUserCrud = new DefaultUserCRUD();
        UserCRUDDTO userDTO = new UserCRUDDTO();
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        roles.add(role);
        userDTO.setRoles(roles);
        Assertions.assertTrue(defaultUserCrud.hasIllegalRole(userDTO));
    }


}