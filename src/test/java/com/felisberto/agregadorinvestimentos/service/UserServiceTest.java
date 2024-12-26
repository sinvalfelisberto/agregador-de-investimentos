package com.felisberto.agregadorinvestimentos.service;

import com.felisberto.agregadorinvestimentos.controller.dto.user.CreateUserDto;
import com.felisberto.agregadorinvestimentos.controller.dto.user.UpdateUserDto;
import com.felisberto.agregadorinvestimentos.entity.User;
import com.felisberto.agregadorinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    //arrange
    //act
    //assert

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("---- Should create a User with sucess ----")
        void shouldCreateAUser() {
            //arrange
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null, null);
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto("username", "email@email.com", "abc123", null);

            //act
            userService.createUser(input);
            var output = userService.createUser(input);

            //Assert
            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());

        }

        @Test
        @DisplayName("---- Should throw exception when error occurs ----")
        void shouldThrowExceptionWhenErrorOccurs() {
            //arrange
            doThrow(new RuntimeException()).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto("username", "email@email.com", "abc123", null);

            //act & assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));

        }


    }
    /*
    @Nested
    class getUserBuId {
        @Test
        @DisplayName("---- Should get user by id on success when optional is present ----")
        void shouldGetUserByIdOnSuccessIsPresent(){
            //Arrange
            var user = new ListUserDto(UUID.randomUUID().toString(), "username", "email@email.com");
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            //Act
            var output = userService.getUserById(user.getUserId());
            //Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("---- Should get user by id on success when optional is empty ----")
        void shouldGetUserByIdOnSuccessIsEmpty(){
            //Arrange
            //var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null);
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());
            //Act
            var output = userService.getUserById(userId.toString());
            //Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class listUsers{
        @Test
        @DisplayName("---- Should return All Users on Success ---")
        void shoulReturnAllUsesOnSuccess() {
            //Arrange
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null, null);
            var userList = List.of(user);
            doReturn(userList).when(userRepository).findAll();
            //Act
            var output = userService.listUsers();
            //Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }
    */


    @Nested
    class updateById {

        @Test
        @DisplayName("---- Should update user by id on success when user is present and username and password is filled ----")
        void shouldUpdateUserByIdOnSuccessWhenUserIsPresentAndUsernameAndPasswordIsFilled(){
            //Arrange
            var updateUserDto = new UpdateUserDto("newUsername", "newpassword");
            var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null, null);
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            //Act
            userService.updateUserById(user.getUserId().toString(),
                    updateUserDto);
            //Assert
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("---- Should update user by id on success when user does not present ----")
        void shouldNotUpdateUserByIdOnSuccessWhenUserDoesNotExists(){
            //Arrange
            var updateUserDto = new UpdateUserDto("newUsername", "newpassword");
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            //Act
            userService.updateUserById(userId.toString(),
                    updateUserDto);
            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).save(any());
        }

    }

    @Nested
    class deleteById{

        @Test
        @DisplayName("---- Should Delete By Id On Success When User Exists---")
        void shouldDeleteByIdOnSuccessWhenUserExists() {

            //Arrange
            //var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null);
            doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
            doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            //Act
            userService.deleteById(userId.toString());

            //Assert
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));
            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));

        }

        @Test
        @DisplayName("---- Should Delete By Id On Success When User Does Not Exists---")
        void shouldDeleteByIdOnSuccessWhenUserDoesNotExists() {

            //Arrange
            //var user = new User(UUID.randomUUID(), "username", "email@email.com", "password", Instant.now(), null);
            doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            //Act
            userService.deleteById(userId.toString());

            //Assert

            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());

        }
    }

}