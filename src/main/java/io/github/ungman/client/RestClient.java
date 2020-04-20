package io.github.ungman.client;

import io.github.ungman.client.utils.RestClientHelper;
import io.github.ungman.pojo.Profile;
import io.github.ungman.pojo.User;
import io.github.ungman.utils.ObjectMapperHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component

public class RestClient {

    private final RestClientHelper restClientHelper;
    private final String URI_CHECK_CONNECTION = "/api/available";
    private final String URI_USER = "/api/user";
    private final String URI_PROFILE = "/api/profile";
    private final String URI_AUTH= "/api/auth";
    private final String URI_GET_PROFILE_TO_SHOW="/api/profile/show/next/";
    private String URI_PROFILE_MATCH;

    private String token="";
    public String username="";

    @Autowired
    public RestClient(RestClientHelper restClientHelper){
        this.restClientHelper=restClientHelper;
        URI_PROFILE_MATCH=String.format("/api/profilematch/%s", username);
    }
    public boolean checkAvailableConnection() {
        return restClientHelper.checkAvailableConnection(URI_CHECK_CONNECTION);
    }

    public User createUser(User user) {
        return postAndReturnData(user, User.class, URI_USER);
    }

    public Profile createProfile(Profile profile) {
        return postAndReturnData(profile, Profile.class, URI_PROFILE);
    }

    private <T> T postAndReturnData(T data, Class<T> tClass, String uri) {
        String jsonRequest = ObjectMapperHelper.writeObjectToString(data);
        String jsonResponse = restClientHelper.post(uri, jsonRequest);
        T responseData = null;
        if (restClientHelper.getStatus().is2xxSuccessful())
            responseData = ObjectMapperHelper.readValueFromString(jsonResponse, tClass);
        return responseData;

    }

    public boolean authUser(User user) {
        String jsonRequest=ObjectMapperHelper.writeObjectToString(user);
        String jsonResponse = restClientHelper.post(URI_AUTH, jsonRequest);
        boolean isAuth=restClientHelper.getStatus().is2xxSuccessful();
        if(isAuth){
            token=ObjectMapperHelper.getField(jsonResponse,"token",String.class);
            username=ObjectMapperHelper.getField(jsonResponse,"username",String.class);
            restClientHelper.getHeaders().add("Authorization", "Bearer "+token);
        }
        return isAuth;
    }

    public boolean checkAuth() {
        return restClientHelper.getHeaders().containsKey("Authorization");
    }

    public Profile getProfile(Long idProfile) {
        String uri;
        if(checkAuth())
            uri=URI_GET_PROFILE_TO_SHOW+username;
        else
            uri=URI_GET_PROFILE_TO_SHOW+idProfile.toString();
        String jsonResponse=restClientHelper.get(uri);
        Profile profile=new Profile();
        profile.setIdUser(-1L);
        if(restClientHelper.getStatus().is2xxSuccessful()){
            profile=ObjectMapperHelper.readValueFromString(jsonResponse,Profile.class);
        }
        return profile;
    }

    public boolean setMatch() {
        URI_PROFILE_MATCH=String.format("/api/profilematch/%s", username);
        restClientHelper.post(URI_PROFILE_MATCH+"/set","");
        return restClientHelper.getStatus().is2xxSuccessful();
    }

    public boolean getMatch(){
        boolean result=false;
        URI_PROFILE_MATCH=String.format("/api/profilematch/%s", username);
        String jsonResponse=restClientHelper.post(URI_PROFILE_MATCH+"/checkMatch","");
        if(restClientHelper.getStatus().is2xxSuccessful())
            result =ObjectMapperHelper.readValueFromString(jsonResponse,Boolean.class);
        return result;
    }

    public List<Profile> getMatchProfiles() {
        URI_PROFILE_MATCH=String.format("/api/profilematch/%s", username);
        String jsonResponse=restClientHelper.get(URI_PROFILE_MATCH+"/getMatches");
        List<Profile> listProfiles = new ArrayList<>();
        if (restClientHelper.getStatus().is2xxSuccessful())
            listProfiles = ObjectMapperHelper.getListObjectFromJson(jsonResponse, Profile.class);
        return listProfiles;
    }

    public boolean deleteAccount() {
        URI_PROFILE_MATCH=String.format("/api/profilematch/%s/del", username);
        deleteData(URI_PROFILE_MATCH);
        deleteData(URI_PROFILE+"/"+username);
        deleteData(URI_USER+"/"+username);
        exitFromAccount();
        return false;
    }
    private boolean deleteData(String uri){
        restClientHelper.delete(uri);
        return restClientHelper.getStatus().is2xxSuccessful();
    }
    public boolean exitFromAccount() {
        restClientHelper.getHeaders().remove("Authorization");
        token="";
        username="";
        return true;
    }

    public Profile editProfile(Profile profile) {
        String uri=URI_PROFILE+"/"+username;
        String jsonRequest=ObjectMapperHelper.writeObjectToString(Profile.class);
        String jsonResponse=restClientHelper.put(uri,jsonRequest);
        Profile profile1=new Profile();
        profile.setIdUser(-1L);
        if(restClientHelper.getStatus().is2xxSuccessful())
            profile1=ObjectMapperHelper.readValueFromString(jsonResponse, Profile.class);
        return profile1;
    }
}

