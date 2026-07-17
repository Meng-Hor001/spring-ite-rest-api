package co.istad.productapisimpledemo.service.impl;


import co.istad.productapisimpledemo.dto.auth.RegisterRequest;
import co.istad.productapisimpledemo.dto.auth.RegisterResponse;
import co.istad.productapisimpledemo.dto.auth.UserUpdateRequest;
import co.istad.productapisimpledemo.dto.user.UserResponse;
import co.istad.productapisimpledemo.entity.Profile;
import co.istad.productapisimpledemo.entity.User;
import co.istad.productapisimpledemo.mapper.UserMapper;
import co.istad.productapisimpledemo.repository.ProfileRepository;
import co.istad.productapisimpledemo.repository.UserRepository;
import co.istad.productapisimpledemo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Literal;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    // client used to create : manage the user in KC
    private final Keycloak keycloak;
    private final UserMapper userMapper;
    @Value("${keycloak.realm}")
    private String realm ;
    @Value("${keycloak.client-id}")
    private String clientId;

    private ClientRepresentation getClientById(String clientId) {
        return keycloak.realm(realm)
                .clients()
                .findByClientId(clientId)
                .stream().findFirst().orElseThrow(
                        () -> new NoSuchElementException("No client with id " + clientId)
                );
    }
    private UserRepresentation createUserInKeycloak( RegisterRequest request) {
       // 1. user representation -> store basic information (idm)
       var userRepresentation = new UserRepresentation();
       userRepresentation.setUsername(request.username());
       userRepresentation.setEmail(request.email());
       userRepresentation.setFirstName(request.firstName());
       userRepresentation.setLastName(request.lastName());

       // emailVerified , enableAccount
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false); // temporary
        userRepresentation.setRequiredActions(List.of("VERIFY_EMAIL"));

        // customize more info of the user in keycloak (optional)
        // you will need to create this inside your keycloak as well
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("gender", List.of(request.gender()));
        attributes.put("biography", List.of(request.biography()));

        userRepresentation.setAttributes(attributes);


        // credential -> password
        var cred = new CredentialRepresentation();
        cred.setTemporary(false); // no need to change the password when first login
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(request.password()); // pass & confirm Pass
       // setting the password for this new user
        userRepresentation.setCredentials(List.of(cred));

        // creating the new object in kc
        var resourceResource = keycloak.realm(realm).users();
        try(var response = resourceResource.create(userRepresentation)){
            // confirm if the user is created  , we will configure more
            if(response.getStatus() == 201) {
                // we will assign them the default role
                // all register use will be in CUSTOMER ROLE
               String userId = CreatedResponseUtil.getCreatedId(response);
               UserResource userResource = keycloak.realm(realm).users().get(userId);
               // assign the ROLE for the user in keycloak
                var client = getClientById(clientId);

                // create role representation ( role inside keycloak)
                var roleRepresentation = keycloak.realm(realm)
                        .clients().get(client.getId())
                        .roles().get("CUSTOMER").toRepresentation();

                // add role to the keycloak user
                userResource.roles()
                        .clientLevel(client.getId())
                        .add(List.of(roleRepresentation));
                log.info("Sending email verification to user: {}",userRepresentation.getEmail());
                userResource.sendVerifyEmail();

                userRepresentation.setId(userId);// keycloak id
                return userRepresentation;
                //return userMapper.toRegisterResponse(userRepresentation);
            }else {
                throw new RuntimeException("Error creating user in keycloak");
            }
            //
        }catch(Exception ex){
            ex.printStackTrace();
            log.error("Error creating user in keycloak", ex);
            throw new RuntimeException("Error creating user in keycloak");
        }
        //return null;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        // ensure that password matches
        if(!request.password().equals(request.confirmedPassword())) {
            throw new RuntimeException("Passwords don't match");
        }
        var kcResponse= createUserInKeycloak(request);
        User user = new User();
        // kcResponse.id() -> normal id not keycloak id
        log.info("Value of KC ID : {}", kcResponse.getId());

        user.setKeycloakId(kcResponse.getId());
        user.setEmail(kcResponse.getEmail());
        user.setUsername(kcResponse.getUsername());

        Profile profile = new Profile();
        profile.setFirstName(kcResponse.getFirstName());
        profile.setLastName(kcResponse.getLastName());
        profile.setGender(request.gender());
        profile.setBio(request.biography());
        profile.setUser(user);

       // profile.setProfileUrl(request.profileUrl);
        user.setProfile(profile);
        var createdUser = userRepository.save(user);
        return userMapper.toRegisterResponse(createdUser);

    }

    // TODO:
    // update the user profile
    // only the profile owner able to update their profile

    public UserResponse updateUser(String keycloakId, UserUpdateRequest request){
        var oldUser = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(()-> new NoSuchElementException("User with id "+keycloakId+" not found"));
        var oldProfile = oldUser.getProfile();
        if (request.firstName() != null)
            oldProfile.setFirstName(request.firstName());
        if (request.lastName() != null)
            oldProfile.setLastName(request.lastName());
        if (request.gender() != null)
            oldProfile.setGender(request.gender());
        if (request.biography() != null)
            oldProfile.setBio(request.biography());
        oldUser.setProfile(oldProfile);

        // update the new data into  spring database
        var updatedUser = userRepository.save(oldUser);

        // 2. update the keycloak side
        try{
            var userResource = keycloak
                    .realm(realm)
                    .users()
                    .get(keycloakId);

            var userRep = userResource.toRepresentation();
            if(request.firstName() != null)
                userRep.setFirstName(request.firstName());
            if(request.lastName() != null)
                userRep.setLastName(request.lastName());
            if(request.gender() != null)
                userRep.setAttributes(Map.of("gender", List.of(request.gender())));
            if(request.biography() != null)
                userRep.setAttributes(Map.of("biography", List.of(request.biography())));
            userResource.update(userRep);

            // gender , biography
            Map<String, List<String>> attributes = (userRep.getAttributes() != null ? new HashMap<>(userRep.getAttributes()) : new HashMap<>());
            if (request.gender() !=null)
                attributes.put("gender", List.of(request.gender()));
            if (request.biography() != null)
                attributes.put("biography", List.of(request.biography()));

            userRep.setAttributes(attributes);
            userResource.update(userRep);
            return userMapper.toUserResponse(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error saving user in keycloak", e);
            throw new RuntimeException(e);
        }

    }

    // reset-password
    public void forgotPassword(String email){
        try{
            var listUserRepresentation = keycloak
                    .realm(realm)
                    .users()
                    .searchByEmail(email, true);

            // extract = true, exact match

            if (listUserRepresentation.isEmpty()){
                log.warn("Sending reset password to non-existent user with email: {}", email);
                return;
            }

            String KcUserId= listUserRepresentation.getFirst().getId();
            var userResource = keycloak.realm(realm).users().get(KcUserId);

            log.info("sending reset password to user with id {}", KcUserId);
            userResource.executeActionsEmail(List.of("UPDATE_PASSWORD"));
        } catch (Exception e) {
            // TODO: handle better exception
            e.printStackTrace();
            log.error("Error saving user in keycloak", e);
            throw new RuntimeException("Error saving user in keycloak");
        }
    }
}
