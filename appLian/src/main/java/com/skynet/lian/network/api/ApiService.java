package com.skynet.lian.network.api;


import com.google.gson.JsonObject;
import com.skynet.lian.models.AddressGeocoding;
import com.skynet.lian.models.ApiResponseGeoCoding;
import com.skynet.lian.models.ChatItem;
import com.skynet.lian.models.Comment;
import com.skynet.lian.models.Contact;
import com.skynet.lian.models.Message;
import com.skynet.lian.models.MyPlace;
import com.skynet.lian.models.Notification;
import com.skynet.lian.models.PlaceNearby;
import com.skynet.lian.models.Post;
import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Room;
import com.skynet.lian.models.Routes;
import com.skynet.lian.models.Term;
import com.skynet.lian.models.Timeline;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by thaopt on 9/6/17.
 */

public interface ApiService {
    public static String API_ROOT = "https://lianchat.site/api/";
//    public static String API_ROOT = "http://lianchat.site/api/";

    @GET("directions/json")
    Call<ApiResponse<List<Routes>>> getDirection(
            @Query("origin") String orgin
            , @Query("destination") String destination
            , @Query("key") String key);

    @GET("place/autocomplete/json")
    Call<ApiResponse<List<MyPlace>>> getAddress(@Query("input") String input
            , @Query("types") String type
            , @Query("strictbounds") boolean strictbounds
            , @Query("location") String location
            , @Query("radius") int radius
            , @Query("key") String key);
    @GET("geocode/json")
    Call<ApiResponseGeoCoding<List<AddressGeocoding>>> getAddress(
            @Query("latlng") String location
            , @Query("key") String key);
    @GET("geocode/json")
    Call<ApiResponse<List<PlaceNearby>>> getLocation(
            @Query("address") String location
            , @Query("sensor") boolean sensor
            , @Query("key") String key
    );

    @GET("place/nearbysearch/json")
    Call<ApiResponse<List<PlaceNearby>>> getNearby(
            @Query("location") String location
            , @Query("radius") int radius
            , @Query("type") String type
            , @Query("limit") int limit
            , @Query("key") String key);
    @GET("place/nearbysearch/json")
    Call<JsonObject> getNearbyJson(
            @Query("location") String location
            , @Query("radius") int radius
            , @Query("type") String type
            , @Query("limit") int limit
            , @Query("key") String key);
    @GET("get_info.php")
    Call<ApiResponse<Profile>> getProfile(@Query("id") String uid);

    @GET("list_friend_online.php")
    Call<ApiResponse<List<Profile>>> getListFriends(@Query("user_id") String uid);

    @GET("list_chat.php")
    Call<ApiResponse<List<ChatItem>>> getListChats(@Query("user_id") String uid);

    @GET("search_chat.php")
    Call<ApiResponse<List<ChatItem>>> getListChats(@Query("user_id") String uid, @Query("key") String key);

    @GET("search_group_chat.php")
    Call<ApiResponse<List<ChatItem>>> getListGroupChats(@Query("user_id") String uid, @Query("key") String key);

    @GET("list_group_chat.php")
    Call<ApiResponse<List<ChatItem>>> getListGroup(@Query("user_id") String uid);

    @GET("list_friend.php")
    Call<ApiResponse<List<Profile>>> getContact(@Query("user_id") String uid);

    @GET("list_block.php")
    Call<ApiResponse<List<Profile>>> getListWhitelist(@Query("user_id") String uid);
    @FormUrlEncoded
    @POST("block.php")
    Call<ApiResponse> block(@Field("user_id") String uid, @Field("user_id_block") String uidBlock, @Field("type") int type);

    @GET("login_user.php")
    Call<ApiResponse<Profile>> login(@Query("phone") String uid);

    @FormUrlEncoded
    @POST("check_contact.php")
    Call<ApiResponse<List<Profile>>> checkContact(@Field("user_id") String uid, @Field("list_contact") String list_contact);

    @FormUrlEncoded
    @POST("check_contact.php")
    Call<ApiResponse<List<Contact>>> checkContacts(@Field("user_id") String uid, @Field("list_contact") String list_contact);

    @FormUrlEncoded
    @POST("accept_friend.php")
    Call<ApiResponse> acceptFriend(@Field("user_id") String uid, @Field("friend_id") String friend_id);

    @FormUrlEncoded
    @POST("create_group.php")
    Call<ApiResponse<ChatItem>> createGroup(@Field("list_user_id") String uid, @Field("title") String title, @Field("user_id_create") String user_id_create);

    @FormUrlEncoded
    @POST("edit_group.php")
    Call<ApiResponse> editGroup(@Field("group_id") String uid, @Field("title") String title);

    @FormUrlEncoded
    @POST("add_friend.php")
    Call<ApiResponse> addFried(@Field("user_id") String uid, @Field("friend_id") String add_friend);

    @FormUrlEncoded
    @POST("unfriend.php")
    Call<ApiResponse> unfriend(@Field("user_id") String uid, @Field("friend_id") String add_friend);

    @GET("find_friend.php")
    Call<ApiResponse<List<Profile>>> queryContact(@Query("user_id") String uid, @Query("phone") String phone);

    @GET("get-list-friend-business")
    Call<ApiResponse<List<Profile>>> getBussinessContact(@Query("idUser") String uid);

    @GET("notification.php")
    Call<ApiResponse<List<Notification>>> getListNotification(@Query("id") String uid, @Query("type") int type);

    @GET("notification_detail.php")
    Call<ApiResponse<Notification>> getDetailNotification(@Query("id") String id, @Query("type") int type, @Query("user_id") String shID);

    @FormUrlEncoded
    @POST("favourite.php")
    Call<ApiResponse> toggleFav(@Field("id_user") String idUser, @Field("id_ex") int idEx, @Field("type") int isFav);

    @GET("term.php")
    Call<ApiResponse<Term>> getTerm();

    @GET("privacy.php")
    Call<ApiResponse<Term>> getPrivacy();


    //    MESSAGE
    @GET("content_chat.php")
    Call<ApiResponse<Room>> getRoomInfo(@Query("user_id_1") String user_id_1, @Query("user_id_2") String user_id_2, @Query("index") int index);

    //    MESSAGE
    @GET("list_post.php")
    Call<ApiResponse<Timeline>> getListTimeline(@Query("user_id_seen") String user_id_seen, @Query("user_id") String user_id, @Query("index") int index);

    //    MESSAGE
    @GET("news_feed.php")
    Call<ApiResponse<Timeline>> getNewsfeed(@Query("user_id_seen") String user_id_seen, @Query("user_id") String user_id, @Query("index") int index);

    //    MESSAGE
    @GET("list_image.php")
    Call<ApiResponse<Timeline>> getListPhotos(@Query("user_id_seen") String user_id_seen, @Query("user_id") String user_id, @Query("index") int index);

    //    MESSAGE
    @GET("list_video.php")
    Call<ApiResponse<Timeline>> getListVideos(@Query("user_id_seen") String user_id_seen, @Query("user_id") String user_id, @Query("index") int index);

    //    MESSAGE
    @GET("profile.php")
    Call<ApiResponse<Profile>> getProfileFriend(@Query("user_id_friend") String idFriend, @Query("user_id_view") String user_id);

    //    MESSAGE
    @GET("content_group_chat.php")
    Call<ApiResponse<Room>> getRoomGroupChatInfo(@Query("user_id") String user_id_1, @Query("group_id") String group_id, @Query("index") int index);

    @FormUrlEncoded
    @POST("chat.php")
    Call<ApiResponse<Message>> chatSingle(@Field("user_id_send") String user_id_send, @Field("user_id_receive") String user_id_receive, @Field("content") String content);

    @FormUrlEncoded
    @POST("like_post.php")
    Call<ApiResponse> toggleLikePost(@Field("user_id") String user_id, @Field("post_id") int post_id, @Field("is_like") int is_like);

    @FormUrlEncoded
    @POST("mute_chat.php")
    Call<ApiResponse> toggleMuteChat(@Field("user_id") String user_id, @Field("chat_id") String chat_id, @Field("type") int type);

    @FormUrlEncoded
    @POST("update_online.php")
    Call<ApiResponse> toggleOnline(@Field("user_id") String user_id, @Field("online") int type);

    @FormUrlEncoded
    @POST("follow.php")
    Call<ApiResponse> toggleFollow(@Field("user_follow_id") String user_id, @Field("user_id") String friend_id, @Field("type") int is_like);

    @FormUrlEncoded
    @POST("active_comment.php")
    Call<ApiResponse> toggleComment(@Field("user_id") String user_id, @Field("post_id") int post_id, @Field("active") int is_like);

    @FormUrlEncoded
    @POST("delete_post.php")
    Call<ApiResponse> deletePost(@Field("user_id") String user_id, @Field("post_id") int post_id);

    @FormUrlEncoded
    @POST("comment_post.php")
    Call<ApiResponse<Comment>> commentPost(@Field("user_id") String user_id, @Field("post_id") int post_id, @Field("content") String is_like);


    @FormUrlEncoded
    @POST("chat_group.php")
    Call<ApiResponse<Message>> chatGroup(@Field("user_id_send") String user_id_send, @Field("group_id") String group_id, @Field("content") String content);


    @Multipart
    @POST("upload_file.php")
    Call<ApiResponse<Message>> uploadFile(@Part("user_id") RequestBody user_id, @Part("chat_id") RequestBody chat_id, @Part("type") RequestBody type, @Part MultipartBody.Part listFile);

    @Multipart
    @POST("upload_image.php")
    Call<ApiResponse<List<Message>>> uploadImages(@Part("user_id") RequestBody user_id, @Part("chat_id") RequestBody chat_id, @Part("type") RequestBody type, @Part List<MultipartBody.Part> listFile);

    @Multipart
    @POST("update_avatar.php")
    Call<ApiResponse<String>> uploadAvatar(@Part MultipartBody.Part image, @PartMap() Map<String, RequestBody> partMap);

    @FormUrlEncoded
    @POST("delete_chat.php")
    Call<ApiResponse> deleteChat(@Field("user_id") String ids, @Field("chat_id") String id);

    @FormUrlEncoded
    @POST("delete_group.php")
    Call<ApiResponse> deleteGroupChat(@Field("group_id") String id, @Field("user_id") String id1);

    @FormUrlEncoded
    @POST("update_fcm.php")
    Call<ApiResponse> updateFCM(@Field("user_id") String u_id, @Field("token_fcm") String tokenFCM);

    @FormUrlEncoded
    @POST("leave_group.php")
    Call<ApiResponse> leaveGroupChat(@Field("group_id") String id, @Field("user_id") String id1);

    @FormUrlEncoded
    @POST("join_group.php")
    Call<ApiResponse<Room>> addUserGroup(@Field("group_id") String id, @Field("user_id") String id1);

    @FormUrlEncoded
    @POST("share_post.php")
    Call<ApiResponse<Integer>> sharePost(@Field("post_id") int id, @Field("user_id") String idUser, @Field("content_share") String content_share, @Field("type_share") int type_share);

    @FormUrlEncoded
    @POST("edit_post.php")
    Call<ApiResponse> editPost(@Field("post_id") int id, @Field("user_id") String idUser, @Field("content") String content, @Field("type_share") int type_share);

    @GET("post_detail.php")
    Call<ApiResponse<Post>> getDetailPost(@Query("post_id") int id, @Query("user_id") String id1);



    @Multipart
    @POST("post.php")
    Call<ApiResponse<Integer>> submitPost(@Part("user_id") RequestBody user_id, @Part("type_share") RequestBody type_share, @Part("address") RequestBody address,
                                          @Part("content") RequestBody content, @Part List<MultipartBody.Part> listFile);

    @Multipart
    @POST("post.php")
    Call<ApiResponse<Integer>> submitPost(@Part("user_id") RequestBody user_id, @Part("type_share") RequestBody type_share, @Part("address") RequestBody address,
                                          @Part("content") RequestBody content);

}
