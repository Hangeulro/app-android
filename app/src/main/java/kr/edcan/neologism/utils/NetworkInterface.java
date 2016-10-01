package kr.edcan.neologism.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.edcan.neologism.model.Board;
import kr.edcan.neologism.model.DicData;
import kr.edcan.neologism.model.FacebookUser;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.model.MyDicViewData;
import kr.edcan.neologism.model.Quiz;
import kr.edcan.neologism.model.User;
import kr.edcan.neologism.model.Word;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by JunseokOh on 2016. 9. 18..
 */
public interface NetworkInterface {

    @POST("/my")
    @FormUrlEncoded
    Call<User> getUserInfo(@Field("token") String token);

    @POST("/my/pointUp")
    @FormUrlEncoded
    Call<ResponseBody> scoreUp(@Field("token") String token, @Field("pointUp") int point);


    @POST("/auth/login")
    @FormUrlEncoded
    Call<ResponseBody> userLogin(@Field("userid") String userid, @Field("pw") String password);

    @POST("/auth/auto")
    @FormUrlEncoded
    Call<ResponseBody> userAutoLogin(@Field("token") String token);

    @POST("/auth/register")
    @FormUrlEncoded
    Call<ResponseBody> userRegister(@Field("userid") String userid, @Field("pw") String password, @Field("name") String username);

    @GET("/auth/fb/token")
    Call<FacebookUser> facebookLogin(@Query("access_token") String accessToken);

    @GET("/auth/tw/token")
    Call<ResponseBody> twitterLogin(
            @Query("oauth_token") String accessToken,
            @Query("oauth_token_secret") String accessTokenSecret,
            @Query("user_id") String userid);

    @GET("/version")
    Call<String> getDataBaseVersion();

    @POST("/word/cata")
    @FormUrlEncoded
    Call<ResponseBody> getWordWithType(@Field("cata") String cata);

    @POST("/word")
    Call<ResponseBody> getWordList();

    @POST("/word/commentAdd")
    @FormUrlEncoded
    Call<DicData> addCommentToWord(@Field("token") String token, @Field("date") Date date,
                                   @Field("wordid") String wordid, @Field("summary") String summary);

    @POST("/quize")
    Call<ArrayList<Quiz>> getQuizList();

    @POST("/mydic")
    @FormUrlEncoded
    Call<ArrayList<MyDic>> getMyDictionary(@Field("token") String token);

    @POST("/mydic/make")
    @FormUrlEncoded
    Call<ResponseBody> createMyDictionary(@Field("token") String token, @Field("dicname") String title
            , @Field("sub") String subTitle);

    @POST("/mydic/add")
    @FormUrlEncoded
    Call<ResponseBody> addToDictionary(@Field("token") String token, @Field("dicname") String dicName
            , @Field("word") String word);

    @POST("/mydic/pop")
    @FormUrlEncoded
    Call<ResponseBody> removeFromDictionary(@Field("token") String token, @Field("dicname") String dicName
            , @Field("id") String wordId);
    @POST("/mydic/detail")
    @FormUrlEncoded
    Call<MyDicViewData> getMyDicInfo(@Field("token") String token, @Field("dicname") String dicName);

    @POST("/board")
    Call<ArrayList<Board>> getBoardList();

    @POST("/board/commentAdd")
    @FormUrlEncoded
    Call<ResponseBody> addCommentToBoard(@Field("token") String token, @Field("boardid") String boardid,
                                         @Field("summary") String comment, @Field("date") Date date);
    @POST("/board/detail")
    @FormUrlEncoded
    Call<Board> getBoardInfo(@Field("boardid") String boardid);

    @POST("/board/write")
    @Multipart
    Call<ResponseBody> postArticle(@Part("file\"; filename=\"image.jpg\" ")RequestBody file, @Part("token") RequestBody token,
                                   @Part("title") RequestBody title, @Part("contents") RequestBody contents);

}
