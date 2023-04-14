package chat.hola.com.app.Networking;

import chat.hola.com.app.calling.model.PendingCallsResponse;
import chat.hola.com.app.coin.model.CoinPackageResponse;
import chat.hola.com.app.collections.model.CollectionPostsResponse;
import chat.hola.com.app.collections.model.CollectionResponse;
import chat.hola.com.app.collections.model.CreateCollectionResponse;
import chat.hola.com.app.comment.model.CommentResponse;
import chat.hola.com.app.hastag.Hash_tag_people_pojo;
import chat.hola.com.app.home.activity.followingTab.model.FollowingResponse;
import chat.hola.com.app.home.activity.youTab.followrequest.ResponseModel;
import chat.hola.com.app.home.activity.youTab.model.ChannelSubscibe;
import chat.hola.com.app.home.activity.youTab.model.YouResponse;
import chat.hola.com.app.home.contact.GetFriendRequest;
import chat.hola.com.app.home.contact.GetFriends;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.model.Posts;
import chat.hola.com.app.home.stories.model.CallHistoryResponse;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.home.stories.model.StoryResponse;
import chat.hola.com.app.home.stories.model.ViewerResponse;
import chat.hola.com.app.live_stream.ResponcePojo.AllGiftsResponse;
import chat.hola.com.app.models.ActionButtonResponse;
import chat.hola.com.app.models.CoinAmountResponse;
import chat.hola.com.app.models.CongnitoResponse;
import chat.hola.com.app.models.CountryResponse;
import chat.hola.com.app.models.CurrencyResponse;
import chat.hola.com.app.models.Documents;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.GiftResponse;
import chat.hola.com.app.models.GuestTokenResponse;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.models.PaymentGatewayResponse;
import chat.hola.com.app.models.PostTypeResponse;
import chat.hola.com.app.models.RechargeResponse;
import chat.hola.com.app.models.Register;
import chat.hola.com.app.models.StripeResponse;
import chat.hola.com.app.models.SuggestedAmountResponse;
import chat.hola.com.app.models.TranResponse;
import chat.hola.com.app.models.TransactionResponse;
import chat.hola.com.app.models.TransferResponse;
import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.models.WithdawStatusResponse;
import chat.hola.com.app.models.WithdrawAmountCheckResponse;
import chat.hola.com.app.models.WithdrawLog;
import chat.hola.com.app.models.WithdrawMethodResponse;
import chat.hola.com.app.models.WithdrawSuccess;
import chat.hola.com.app.post.ReportReason;
import chat.hola.com.app.post.model.CategoryResponse;
import chat.hola.com.app.post.model.ChannelResponse;
import chat.hola.com.app.profileScreen.addChannel.model.AddChannelResponse;
import chat.hola.com.app.profileScreen.addChannel.model.ChannelBody;
import chat.hola.com.app.profileScreen.discover.contact.pojo.ContactRequest;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileBody;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileResponse;
import chat.hola.com.app.profileScreen.followers.Model.FollowersResponse;
import chat.hola.com.app.profileScreen.model.BusinessCategory;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.profileScreen.story.model.ProfilePostRequest;
import chat.hola.com.app.request_star_profile.model.StarStatusResponse;
import chat.hola.com.app.request_star_profile.star_category.StarCatResponse;
import chat.hola.com.app.search.model.SearchResponse;
import chat.hola.com.app.search.tags.module.TagsResponse;
import chat.hola.com.app.socialDetail.model.PostResponse;
import chat.hola.com.app.subscription.model.SubscriptionListRes;
import chat.hola.com.app.transfer_to_friend.model.TransactionDetailMain;
import chat.hola.com.app.ui.withdraw.model.bankaccount.BankAccountResponse;
import chat.hola.com.app.ui.withdraw.model.bankfield.BankFieldResponse;
import chat.hola.com.app.wallet.transaction.Model.TransactionMain;
import io.reactivex.Observable;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * <h1>HowdooService</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 21/2/18.
 */

public interface HowdooService {

    @POST("post/media")
    Observable<Response<Data>> createPost(@Header("authorization") String auth,
                                          @Header("lang") String lang,
                                          @Body Map<String, Object> map);

    @POST("logout")
    Observable<Response<ResponseBody>> logout(@Header("authorization") String auth,
                                              @Header("lang") String lang);


    @GET("hashTags")
    Observable<Response<Hash_tag_people_pojo>> checkHashTag(@Header("authorization") String auth,
                                                            @Header("lang") String lang,
                                                            @Query("hashtag") String hahashtag);

    @GET("userTag")
    Observable<Response<Hash_tag_people_pojo>> checkUserTag(@Header("authorization") String auth,
                                                            @Header("lang") String lang,
                                                            @Query("userName") String usertag);

    @GET("home/")
    Observable<Response<Posts>> getPosts(@Header("authorization") String auth,
                                         @Header("lang") String lang,
                                         @Query("offset") int offset,
                                         @Query("limit") int limit);

    @GET("home/guestUser")
    Observable<Response<Posts>> getGuestPosts(@Header("authorization") String auth,
                                              @Header("lang") String lang,
                                              @Query("offset") int offset,
                                              @Query("limit") int limit);

    @GET("profile/member")
    Observable<Response<Profile>> getMember(@Header("authorization") String auth,
                                            @Header("lang") String lang,
                                            @Query("memberId") String memberId);

    @GET("profile/memberPosts")
    Observable<Response<ProfilePostRequest>> getMemberPosts(@Header("authorization") String auth,
                                                            @Header("lang") String lang,
                                                            @Query("memberId") String memberId,
                                                            @Query("skip") int skip,
                                                            @Query("limit") int limit);

    @GET("profile/users")
    Observable<Response<Profile>> getUserProfile(@Header("authorization") String auth,
                                                 @Header("lang") String lang);

    @GET("profile/posts")
    Observable<Response<ProfilePostRequest>> getProfilePost(@Header("authorization") String auth,
                                                            @Header("lang") String lang,
                                                            @Query("skip") int skip,
                                                            @Query("limit") int limit);

    @GET("storyHistory")
    Observable<Response<StoryResponse>> getUserStoriesHistory(@Header("authorization") String auth,
                                                              @Header("lang") String lang,
                                                              @Query("targetUserId") String targetUserId);

    @GET("category")
    Observable<Response<CategoryResponse>> getCategories(@Header("authorization") String auth,
                                                         @Header("lang") String lang);

    @GET("channel")
    Observable<Response<ChannelResponse>> getChannels(@Header("authorization") String auth,
                                                      @Header("lang") String lang);

    @GET("search/people")
    Observable<Response<SearchResponse>> search(@Header("authorization") String auth,
                                                @Header("lang") String lang,
                                                @Query("username") String username);

    @GET("hashTagList")
    Observable<Response<TagsResponse>> searchTags(@Header("authorization") String auth,
                                                  @Header("lang") String lang,
                                                  @Query("hashtag") String hashtag);

    @GET("channelList")
    Observable<Response<chat.hola.com.app.search.channel.module.ChannelResponse>> searchChannel(@Header("authorization") String auth,
                                                                                                @Header("lang") String lang,
                                                                                                @Query("searchText") String channel);

    @POST("follow")
    Observable<Response<FollowResponse>> follow(@Header("authorization") String auth,
                                                @Header("lang") String lang,
                                                @Body Map<String, Object> followingId);

    @POST("unfollow")
    Observable<Response<FollowResponse>> unfollow(@Header("authorization") String auth,
                                                  @Header("lang") String lang,
                                                  @Body Map<String, Object> followingId);

    @GET("post")
    Observable<Response<PostResponse>> postDetail(@Header("authorization") String auth,
                                                  @Header("lang") String lang,
                                                  @Query("postId") String postId,
                                                  @Query("city") String city,
                                                  @Query("country") String country,
                                                  @Query("ipaddress") String ipaddress,
                                                  @Query("latitude") Double latitude,
                                                  @Query("longitude") Double longitude);

    @POST("User/Contacts")
    Observable<Response<ResponseBody>> postUser(@Header("authorization") String auth,
                                                @Header("token") String token,
                                                @Body ContactRequest request);

    //createChannel
    @POST("channel")
    Observable<Response<AddChannelResponse>> createChannel(@Header("authorization") String auth,
                                                           @Header("lang") String lang,
                                                           @Body ChannelBody Channelbody);

    //editProfile
    @PATCH("profile")
    Observable<Response<EditProfileResponse>> editProfile(@Header("authorization") String auth,
                                                          @Header("lang") String lang,
                                                          @Body EditProfileBody profileBody);

    @PATCH("profile")
    Observable<Response<EditProfileResponse>> editProfile(@Header("authorization") String auth,
                                                          @Header("lang") String lang,
                                                          @Body Map<String, Object> profileBody);

    @PATCH("profile")
    Call<ResponseBody> editProfile1(@Header("authorization") String auth,
                                    @Header("lang") String lang,
                                    @Body Map<String, Object> profileBody);

    @GET("follow/followers")
    Observable<Response<FollowersResponse>> getFollowers(@Header("authorization") String auth,
                                                         @Header("lang") String lang,
                                                         @Query("userId") String userId,
                                                         @Query("skip") int skip,
                                                         @Query("limit") int limit);

    @GET("follow/followees")
    Observable<Response<FollowersResponse>> getFollowees(@Header("authorization") String auth,
                                                         @Header("lang") String lang,
                                                         @Query("userId") String userId,
                                                         @Query("skip") int skip,
                                                         @Query("limit") int limit);

    @POST("like")
    Observable<Response<ResponseBody>> like(@Header("authorization") String auth,
                                            @Header("lang") String lang,
                                            @Body Map<String, String> map);

    @POST("unlike")
    Observable<Response<ResponseBody>> unlike(@Header("authorization") String auth,
                                              @Header("lang") String lang,
                                              @Body Map<String, String> map);

    @GET("profile/channel")
    Observable<Response<chat.hola.com.app.profileScreen.channel.Model.ChannelResponse>> getChannelPost(@Header("authorization") String auth,
                                                                                                       @Header("lang") String lang,
                                                                                                       @Query("userId") String userId,
                                                                                                       @Query("skip") int skip,
                                                                                                       @Query("limit") int limit);

    @POST("follow/all")
    Observable<Response<ResponseBody>> followAll(@Header("authorization") String auth,
                                                 @Header("lang") String lang,
                                                 @Body Map<String, List<String>> map);

    @GET("activity")
    Observable<Response<YouResponse>> getYouActivity(@Header("authorization") String auth,
                                                     @Header("lang") String lang,
                                                     @Query("offset") int offset,
                                                     @Query("limit") int limit);

    @POST("comment")
    Observable<Response<CommentResponse>> addComment(@Header("authorization") String auth,
                                                     @Header("lang") String lang,
                                                     @Body Map<String, Object> map);

    //@DELETE("comment")
    @HTTP(method = "DELETE", path = "comment", hasBody = true)
    Observable<Response<CommentResponse>> deleteComment(@Header("authorization") String auth,
                                                        @Header("lang") String lang,
                                                        @Body Map<String, Object> map);

    @POST("comment/like")
    Observable<Response<CommentResponse>> likeComment(@Header("authorization") String auth,
                                                      @Header("lang") String lang,
                                                      @Body Map<String, Object> map);

    @GET("comment")
    Observable<Response<chat.hola.com.app.comment.model.Response>> getComment(@Header("authorization") String auth,
                                                                              @Header("lang") String lang,
                                                                              @Query("postId") String postId,
                                                                              @Query("offset") int skip,
                                                                              @Query("limit") int limit);

    @PUT("subscribeChannel")
    Observable<Response<ResponseBody>> subscribeChannel(@Header("authorization") String auth,
                                                        @Header("lang") String lang,
                                                        @Body Map<String, Object> map);

    @PUT("unSubscribeChannel")
    Observable<Response<ResponseBody>> unSubscribeChannel(@Header("authorization") String auth,
                                                          @Header("lang") String lang,
                                                          @Body Map<String, Object> map);


    @GET("postsByChannel")
    Observable<Response<chat.hola.com.app.trendingDetail.model.ChannelResponse>> getChannelById(@Header("authorization") String auth,
                                                                                                @Header("lang") String lang,
                                                                                                @Query("channelId") String channelId);

    @GET("postsByHashTag")
    Observable<Response<chat.hola.com.app.profileScreen.channel.Model.ChannelData>> getPostByHashtag(@Header("authorization") String auth,
                                                                                                     @Header("lang") String lang,
                                                                                                     @Query("hashTag") String hashTag);

    @GET("postsByHashTag")
    Observable<Response<chat.hola.com.app.music.Response>> getPostByHashtags(@Header("authorization") String auth,
                                                                             @Header("lang") String lang,
                                                                             @Query("hashTag") String hashTag,
                                                                             @Query("offset") int skip,
                                                                             @Query("limit") int limit);

    @GET("postsByCategory")
    Observable<Response<chat.hola.com.app.profileScreen.channel.Model.ChannelData>> getPostByCategoryId(@Header("authorization") String auth,
                                                                                                        @Header("lang") String lang,
                                                                                                        @Query("categoryId") String categoryId);

    @GET("postsByPlace")
    Observable<Response<chat.hola.com.app.profileScreen.channel.Model.ChannelData>> getPostByLocation(@Header("authorization") String auth,
                                                                                                      @Header("lang") String lang,
                                                                                                      @Query("place") String place);

    @DELETE("post")
    Observable<Response<ResponseBody>> deletePost(@Header("authorization") String auth,
                                                  @Header("lang") String lang,
                                                  @Query("postId") String postId);

    @DELETE("story")
    Observable<Response<ResponseBody>> deleteStory(@Header("authorization") String auth,
                                                   @Header("lang") String lang,
                                                   @Query("storyId") String postId);

    @PUT("post")
    Observable<Response<ResponseBody>> updatePost(@Header("authorization") String auth,
                                                  @Header("lang") String lang,
                                                  @Body Map<String, Object> map);

    @GET("reportReasons")
    Observable<Response<ReportReason>> getReportReason(@Header("authorization") String auth,
                                                       @Header("lang") String lang);

    @POST("reportReasons")
    Observable<Response<ResponseBody>> reportPost(@Header("authorization") String auth,
                                                  @Header("lang") String lang,
                                                  @Body Map<String, String> map);

    // STORY
    @GET("myStory")
    Observable<Response<StoryResponse>> getMyStories(@Header("authorization") String auth,
                                                     @Header("lang") String lang);

    @GET("story/contact")
    Observable<Response<StoryResponse>> getStories(@Header("authorization") String auth,
                                                   @Header("lang") String lang,
                                                   @Query("skip") int skip,
                                                   @Query("limit") int limit);


    @POST("story")
    Observable<Response<StoryPost>> postStory(@Header("authorization") String auth,
                                              @Header("lang") String lang,
                                              @Body Map<String, Object> map);

    @PATCH("story")
    Observable<Response<ResponseBody>> viewStory(@Header("authorization") String auth,
                                                 @Header("lang") String lang,
                                                 @Query("storyId") String storyId);

    @GET("story/viewList")
    Observable<Response<ViewerResponse>> getStoryViewerList(@Header("authorization") String auth,
                                                            @Header("lang") String lang,
                                                            @Query("storyId") String storyId,
                                                            @Query("skip") int skip,
                                                            @Query("limit") int limit);

    // CHANNEL
    @GET("RequestedChannels")
    Observable<Response<ChannelSubscibe>> getRequestedChannels(@Header("authorization") String auth,
                                                               @Header("lang") String lang);

    @PUT("RequestedChannels")
    Observable<Response<ResponseBody>> channelSubsciptionAction(@Header("authorization") String auth,
                                                                @Header("lang") String lang,
                                                                @Body Map<String, Object> params);

    @GET("followRequest")
    Observable<Response<ResponseModel>> getfollowRequest(@Header("authorization") String auth,
                                                         @Header("lang") String lang,
                                                         @Query("offset") int skip,
                                                         @Query("limit") int limit);

    @POST("followRequest")
    Observable<Response<ResponseBody>> requestAction(@Header("authorization") String auth,
                                                     @Header("lang") String lang,
                                                     @Body Map<String, Object> params);

    @GET("followingAcitivty")
    Observable<Response<FollowingResponse>> followingActivities(@Header("authorization") String auth,
                                                                @Header("lang") String lang,
                                                                @Query("offset") int skip,
                                                                @Query("limit") int limit);

    @GET("userReportReasons")
    Observable<Response<ReportReason>> userReportReasons(@Header("authorization") String auth,
                                                         @Header("lang") String lang);

    @POST("userReportReasons")
    Observable<Response<ResponseBody>> reportUser(@Header("authorization") String auth,
                                                  @Header("lang") String lang,
                                                  @Body Map<String, String> map);

    @PUT("channel")
    Observable<Response<AddChannelResponse>> updateChannel(@Header("authorization") String auth,
                                                           @Header("lang") String lang,
                                                           @Body Map<String, Object> map);

    @GET("blockReasons")
    Observable<Response<ReportReason>> getBlockReason(@Header("authorization") String auth,
                                                      @Header("lang") String lang);

    @POST("block")
    Observable<Response<ResponseBody>> block(@Header("authorization") String auth,
                                             @Header("lang") String lang,
                                             @Body Map<String, String> map);

    @POST("favouriteMusic")
    Observable<Response<ReportReason>> favourite(@Header("authorization") String auth,
                                                 @Header("lang") String lang,
                                                 @Body Map<String, Object> map);

    @GET("blocks")
    Observable<Response<chat.hola.com.app.blockUser.model.Response>> getBlockedUser(@Header("authorization") String auth,
                                                                                    @Header("lang") String lang,
                                                                                    @Query("offset") int skip,
                                                                                    @Query("limit") int limit);

    @GET("postsByMusic")
    Observable<Response<chat.hola.com.app.music.Response>> getPostByMusicId(@Header("authorization") String auth,
                                                                            @Header("lang") String lang,
                                                                            @Query("musicId") String channelId);

    @GET("MyLikedPost")
    Observable<Response<ProfilePostRequest>> getLikedPosts(@Header("authorization") String auth,
                                                           @Header("lang") String lang,
                                                           @Query("userId") String userId,
                                                           @Query("skip") int skip,
                                                           @Query("limit") int limit);

    @GET("profile/users/mention")
    Observable<Response<ProfilePostRequest>> getTaggedPosts(@Header("authorization") String auth,
                                                            @Header("lang") String lang,
                                                            @Query("userName") String userName,
                                                            @Query("skip") int skip,
                                                            @Query("limit") int limit);

    @POST("friend")
    Observable<Response<ReportReason>> addFriend(@Header("authorization") String auth,
                                                 @Header("lang") String lang,
                                                 @Body Map<String, Object> map);

    @POST("friendRequestResponce")
    Observable<Response<ReportReason>> requestResponse(@Header("authorization") String auth,
                                                       @Header("lang") String lang,
                                                       @Body Map<String, Object> map);


    @GET("friend")
    Observable<Response<GetFriends>> getFriends(@Header("authorization") String auth,
                                                @Header("lang") String lang);

    @GET("followersFollowee")
    Observable<Response<GetFriends>> getFollowersFollowee(@Header("authorization") String auth,
                                                          @Header("lang") String lang);

    @GET("friend")
    Observable<Response<GetFriends>> searchFriends(@Header("authorization") String auth,
                                                   @Header("lang") String lang,
                                                   @Query("searchText") String text);

    @GET("friendRequest")
    Observable<Response<GetFriendRequest>> getFriendRequests(@Header("authorization") String auth,
                                                             @Header("lang") String lang);

    @GET("starCategories")
    Observable<Response<StarCatResponse>> getStarCategory(@Header("authorization") String auth,
                                                          @Header("lang") String lang);

    @GET("starStatus")
    Observable<Response<StarStatusResponse>> getStarStatus(@Header("authorization") String auth,
                                                           @Header("lang") String lang);


    @POST("StarRequest")
    Observable<Response<Error>> requestStarProfile(@Header("authorization") String auth,
                                                   @Header("lang") String lang,
                                                   @Body Map<String, Object> map);

    @PATCH("starUserChat")
    Observable<Response<ResponseBody>> makeChatVisibity(@Header("authorization") String auth,
                                                        @Header("lang") String lang,
                                                        @Body Map<String, Object> map);

    @GET("friendRequest")
    Observable<Response<GetFriendRequest>> getFriendRequestsSearch(@Header("authorization") String auth,
                                                                   @Header("lang") String lang,
                                                                   @Query("searchText") String text);

    @DELETE("friend")
    Observable<Response<ResponseBody>> unfriend(@Header("authorization") String auth,
                                                @Header("lang") String lang,
                                                @Query("targetUserId") String postId);

    @GET("starUsers")
    Observable<Response<GetFriends>> stars(@Header("authorization") String auth,
                                           @Header("lang") String lang,
                                           @Query("offset") int skip,
                                           @Query("limit") int limit);

    @GET("topStarUsers")
    Observable<Response<SearchResponse>> searchTopStar(@Header("authorization") String auth,
                                                       @Header("lang") String lang,
                                                       @Query("category") int category,
                                                       @Query("offset") int skip,
                                                       @Query("limit") int limit);

    @GET("starUsers")
    Observable<Response<SearchResponse>> searchStar(@Header("authorization") String auth,
                                                    @Header("lang") String lang,
                                                    @Query("searchText") String text,
                                                    @Query("offset") int skip,
                                                    @Query("limit") int limit);

    @GET("starUsersWhomIFollow")
    Observable<Response<SearchResponse>> myStars(@Header("authorization") String auth,
                                                 @Header("lang") String lang,
                                                 @Query("offset") int skip,
                                                 @Query("limit") int limit);

    @GET("likedUserList")
    Observable<Response<FollowersResponse>> likers(@Header("authorization") String auth,
                                                   @Header("lang") String lang,
                                                   @Query("postId") String userId,
                                                   @Query("skip") int skip,
                                                   @Query("limit") int limit);

    @GET("followers")
    Observable<Response<FollowersResponse>> searchFollowers(@Header("authorization") String auth,
                                                            @Header("lang") String lang,
                                                            @Query("userId") String userId,
                                                            @Query("searchText") String searchText,
                                                            @Query("offset") int skip,
                                                            @Query("limit") int limit);

    @GET("followees")
    Observable<Response<FollowersResponse>> searchFollowees(@Header("authorization") String auth,
                                                            @Header("lang") String lang,
                                                            @Query("userId") String userId,
                                                            @Query("searchText") String searchText,
                                                            @Query("offset") int offset,
                                                            @Query("limit") int limit);

    @GET("likedUserList")
    Observable<Response<FollowersResponse>> searchLikers(@Header("authorization") String auth,
                                                         @Header("lang") String lang,
                                                         @Query("postId") String userId,
                                                         @Query("searchText") String searchText,
                                                         @Query("skip") int skip,
                                                         @Query("limit") int limit);


    /* debitOrCredit 0 for all, 1 for debit and 2 for credit */
    @GET("WalletStatement")
    Observable<Response<TransactionMain>> getTransactionList(@Header("authorization") String apiToken,
                                                             @Header("lang") String language,
                                                             @Query("debitOrCredit") int type,
                                                             @Query("skip") int skip,
                                                             @Query("limit") int limit);

    @GET("messageDetails")
    Observable<Response<TransactionDetailMain>> getTransactionDetail(@Header("authorization") String apiToken,
                                                                     @Header("lang") String language,
                                                                     @Query("messageId") String messageId);

    @POST("signUp")
    Observable<Response<Login>> signUp(@Header("authorization") String auth,
                                       @Body Register register);

    @POST("verifyOtp")
    Observable<Response<Login>> loginByOtp(@Header("Authorization") String auth,
                                           @Body Login login);

    @POST("login")
    Observable<Response<Login>> loginByPassword(@Header("authorization") String auth,
                                                @Body Map<String, String> map);

    @POST("login")
    Observable<Response<Login>> loginByUserName(@Header("authorization") String auth,
                                                @Body Map<String, String> map);


    @GET("postViews")
    Observable<Response<FollowersResponse>> viewers(@Header("authorization") String auth,
                                                    @Header("lang") String lang,
                                                    @Query("postId") String userId,
                                                    @Query("skip") int skip,
                                                    @Query("limit") int limit);

    @GET("postViews")
    Observable<Response<FollowersResponse>> searchViewers(@Header("authorization") String auth,
                                                          @Header("lang") String lang,
                                                          @Query("postId") String userId,
                                                          @Query("searchText") String searchText,
                                                          @Query("skip") int skip,
                                                          @Query("limit") int limit);

    @POST("verifyResetOTP")
    Observable<Response<Error>> verifyOtpForgotPws(@Body Map<String, String> map);

    @POST("newPassword")
    Observable<Response<Error>> resetPassword(@Header("authorization") String auth,
                                              @Body Map<String, String> params);

    @POST("RequestOTP")
    Observable<Response<ResponseBody>> requestOtp(@Header("authorization") String auth,
                                                  @Body Map<String, Object> map);

    @PATCH("password")
    Observable<Response<Error>> changePassword(@Header("authorization") String auth,
                                               @Header("lang") String lang,
                                               @Body Map<String, Object> map);


    @POST("verifySignUp")
    Observable<Response<ResponseBody>> verifySignUp(@Header("authorization") String auth,
                                                    @Body Map<String, Object> params);


    @POST("requestEmailVerification")
    Observable<Response<ResponseBody>> requestOtpForEmail(@Header("authorization") String auth,
                                                          @Body Map<String, Object> params);

    @GET("accessToken")
    Observable<Response<Login>> newSessionToken(@Header("authorization") String auth,
                                                @Header("refreshtoken") String refreshToken,
                                                @Header("lang") String lang);

    @GET("isRegisteredNumber")
    Observable<Response<ResponseBody>> verifyIsMobileRegistered(@Header("authorization") String auth,
                                                                @Query("countryCode") String countryCode,
                                                                @Query("phoneNumber") String phoneNumber,
                                                                @Header("lang") String lang);

    /**
     * Gets list of shared live stream
     *
     * @param auth   : authentication token
     * @param lang   : language
     * @param offset : offset
     * @param limit  : limit
     * @return : list of data
     */
    @GET("streamHistory")
    Observable<Response<StoryData>> streamHistory(@Header("authorization") String auth,
                                                  @Header("lang") String lang,
                                                  @Query("userId") String userId,
                                                  @Query("offset") int offset,
                                                  @Query("limit") int limit);


    @POST("BusinessRequest")
    Observable<Response<Error>> applyBusinessProfile(@Header("authorization") String auth,
                                                     @Header("lang") String lang,
                                                     @Body Map<String, Object> params);

    /**
     * Switch the profile
     *
     * @param auth :authentication token
     * @param lang : language
     * @param map  : set of parameters
     *             businessCategoryId : business category id
     *             isActiveBusinessProfile : true : business profile, false : normal profile
     * @return : response
     */
    @PATCH("business")
    Observable<Response<Login>> switchBusiness(@Header("authorization") String auth,
                                               @Header("lang") String lang,
                                               @Body Map<String, Object> map);

    /**
     * It fetches the business category details
     *
     * @param auth : authentication token
     * @param lang : language
     * @return : business categories details
     */
    @GET("businessCategories")
    Observable<Response<BusinessCategory>> businessCategories(@Header("authorization") String auth,
                                                              @Header("lang") String lang);

    /**
     * It send verification code to given email
     *
     * @param auth   : authentication token
     * @param lang   : language
     * @param params :  email address
     * @return is verification code is sent or not
     */
    @POST("businessEmailVerification")
    Observable<Response<ResponseBody>> businessEmailVerification(@Header("authorization") String auth,
                                                                 @Header("lang") String lang,
                                                                 @Body Map<String, String> params);

    /**
     * It send verification code to given phone number
     *
     * @param auth   : authentication token
     * @param lang   : language
     * @param params :  country code and phone number
     * @return is verification code is sent or not
     */
    @POST("businessPhoneVerification")
    Observable<Response<ResponseBody>> businessPhoneVerification(@Header("authorization") String auth,
                                                                 @Header("lang") String lang,
                                                                 @Body Map<String, String> params);

    /**
     * It verifies the entered verification code
     *
     * @param auth   : authentication token
     * @param lang   : language
     * @param params :  verification code and email address
     * @return is verified or not
     */
    @POST("businessEmailOtpVerify")
    Observable<Response<ResponseBody>> businessEmailOtpVerify(@Header("authorization") String auth,
                                                              @Header("lang") String lang,
                                                              @Body Map<String, Object> params);

    /**
     * It verifies the entered verification code
     *
     * @param auth   : authentication token
     * @param lang   : language
     * @param params : verification code, country code and phone number
     * @return is verified or not
     */
    @POST("businessPhoneOtpVerify")
    Observable<Response<ResponseBody>> businessPhoneOtpVerify(@Header("authorization") String auth,
                                                              @Header("lang") String lang,
                                                              @Body Map<String, Object> params);

    /**
     * It verifies modifies the business configuration
     *
     * @param auth   : authentication token
     * @param lang   : language
     * @param params : verification code, country code and phone number
     */
    @PUT("BusinessRequest")
    Observable<Response<ResponseBody>> businessConfiguration(@Header("authorization") String auth,
                                                             @Header("lang") String lang,
                                                             @Body Map<String, Object> params);


    /**
     * To get list of currency
     *
     * @param auth   : authentication token
     * @param lang   : language
     * @param offset : offset for pagination
     * @param limit  : limit for pagination
     * @return : currency list
     */
    @GET("currency")
    Observable<Response<CurrencyResponse>> currency(@Header("authorization") String auth,
                                                    @Header("lang") String lang,
                                                    @Query("offset") int offset,
                                                    @Query("limit") int limit);


    /**
     * To get list of business button text
     *
     * @param auth   : authentication token
     * @param lang   : language
     * @param offset : offset for pagination
     * @param limit  : limit for pagination
     * @return : action button list
     */
    @GET("bizButtons")
    Observable<Response<ActionButtonResponse>> buttonText(@Header("authorization") String auth,
                                                          @Header("lang") String lang,
                                                          @Query("offset") int offset,
                                                          @Query("limit") int limit);

    /**
     * To get list of business post type
     *
     * @param auth   : authentication token
     * @param lang   : language
     * @param offset : offset for pagination
     * @param limit  : limit for pagination
     * @return : action post type list
     */
    @GET("businessProductType")
    Observable<Response<PostTypeResponse>> postType(@Header("authorization") String auth,
                                                    @Header("lang") String lang,
                                                    @Query("offset") int offset,
                                                    @Query("limit") int limit);

    @DELETE("channel")
    Observable<Response<ResponseBody>> deleteChannel(@Header("authorization") String auth,
                                                     @Header("lang") String lang,
                                                     @Query("channelId") String channelId);

    /* This is collections APIS.*/

    @POST("bookmark")
    Observable<Response<ResponseBody>> postBookmark(@Header("authorization") String apiToken,
                                                    @Header("lang") String language,
                                                    @Body Map<String, Object> params);

    @DELETE("bookmark")
    Observable<Response<ResponseBody>> deleteToBookmark(@Header("authorization") String apiToken,
                                                        @Header("lang") String language,
                                                        @Query("postId") String postId);

    @POST("collection")
    Observable<Response<CreateCollectionResponse>> createCollection(@Header("authorization") String apiToke,
                                                                    @Header("lang") String lang,
                                                                    @Body Map<String, Object> params);

    @GET("collections")
    Observable<Response<CollectionResponse>> getCollections(@Header("authorization") String apiToken,
                                                            @Header("lang") String language,
                                                            @Query("offset") int offSet,
                                                            @Query("limit") int limit);

    @GET("bookmark")
    Observable<Response<chat.hola.com.app.collections.model.PostResponse>> getAllPost(@Header("authorization") String apiToken,
                                                                                      @Header("lang") String language,
                                                                                      @Query("offset") int offset,
                                                                                      @Query("limit") int limit);

    @GET("collection")
    Observable<Response<chat.hola.com.app.collections.model.PostResponse>> getCollectionPost(@Header("authorization") String apiToken,
                                                                                             @Header("lang") String language,
                                                                                             @Query("collectionId") String collectionId,
                                                                                             @Query("offset") int offset,
                                                                                             @Query("limit") int limit);

    //This API is used to get all bookmarked posts that are not in that collection
    @GET("bookmarkById")
    Observable<Response<chat.hola.com.app.collections.model.PostResponse>> getAllBookMarkById(@Header("authorization") String apiToken,
                                                                                              @Header("lang") String language,
                                                                                              @Query("collectionId") String collectionId,
                                                                                              @Query("offset") int offset,
                                                                                              @Query("limit") int limit);

    @PUT("addToCollection")
    Observable<Response<ResponseBody>> addToCollection(@Header("authorization") String apiToken,
                                                       @Header("lang") String language,
                                                       @Body Map<String, Object> map);

    @PUT("collection")
    Observable<Response<ResponseBody>> editCollection(@Header("authorization") String apiToken,
                                                      @Header("lang") String language,
                                                      @Body Map<String, Object> map);

    @DELETE("collection")
    Observable<Response<ResponseBody>> deleteCollection(@Header("authorization") String apiToken,
                                                        @Header("lang") String language,
                                                        @Query("collectionId") String collectionId);

    @GET("follow/status")
    Observable<Response<Error>> following(@Header("authorization") String apiToken,
                                          @Header("lang") String language,
                                          @Query("userId") String offset);


    /**
     * Wallet to create or update user detail
     *
     * @param apiToken : token
     * @param language : language
     * @param map      : parameters
     *                 userId  : string
     *                 userType : customer, app, provider : string
     *                 userName : string
     *                 email : string
     *                 firstName : string
     *                 lastName : string
     *                 phoneNo : string
     *                 documentName : string
     *                 documentNumber : string
     *                 documentHolderName : string
     *                 documentFrontImage : string
     *                 documentBackImage : string
     * @return : result
     */
    @POST("userDetail")
    Observable<Response<ResponseBody>> userDetail(@Header("authorization") String apiToken,
                                                  @Header("lan") String language,
                                                  @Body Map<String, Object> map);

    /**
     * Provides list of document type
     *
     * @param apiToken : token
     * @param language : language
     * @return : list of document
     */
    @GET("documentList")
    Observable<Response<Documents>> documentType(@Header("authorization") String apiToken,
                                                 @Header("lan") String language);


    //WALLET
    @GET("verificationStatus")
    Observable<Response<Error>> kycVerification(@Header("authorization") String apiToken,
                                                @Header("lan") String language);

    @PATCH("verificationStatus")
    Observable<Response<Error>> approveVerification(@Header("authorization") String apiToken,
                                                    @Header("lan") String language,
                                                    @Body Map<String, Object> map);


    @GET("wallet")
    Observable<Response<WalletResponse>> walletBalance(@Header("authorization") String apiToken,
                                                       @Header("lan") String language,
                                                       @Query("userId") String userId,
                                                       @Query("userType") String userType);

    @GET("paymentGateways")
    Observable<Response<PaymentGatewayResponse>> paymentGateways(@Header("authorization") String apiToken,
                                                                 @Header("lan") String language,
                                                                 @Query("userCurrency") String userCurrency,
                                                                 @Query("status") String status,
                                                                 @Query("limit") Integer limit,
                                                                 @Query("skip") Integer skip);

    @POST("walletTransaction/transfer")
    Observable<Response<TransferResponse>> transferTo(@Header("authorization") String apiToken,
                                                      @Header("lan") String language,
                                                      @Body Map<String, Object> params);

    /**
     * Returns list of all transactions data
     *
     * @param apiToken  : token
     * @param language  : language
     * @param walletId  : wallet id
     * @param type      : transaction type, 0= all, 1=credit, 2=debit
     * @param pageState : paging state
     * @param fetchSize : paging data size
     * @return list of transaction detail
     */
    @GET("walletTransaction")
    Observable<Response<TransactionResponse>> transactions(@Header("authorization") String apiToken,
                                                           @Header("lan") String language,
                                                           @Query("walletId") String walletId,
                                                           @Query("txnType") Integer type,
                                                           @Query("pageState") String pageState,
                                                           @Query("fetchSize") Integer fetchSize);

    @GET("bankFields")
    Observable<Response<BankFieldResponse>> getBankFields(@Header("authorization") String accessToken,
                                                          @Header("lan") String language,
                                                          @Query("paymentGatewayId") String paymentGateWayId);

    @POST("bank")
    Observable<Response<ResponseBody>> addBankAccount(@Header("authorization") String accessToken,
                                                      @Header("lan") String language,
                                                      @Body Map<String, Object> body);

    @GET("bank")
    Observable<Response<BankAccountResponse>> getBanks(@Header("authorization") String accessToken,
                                                       @Header("lan") String language,
                                                       @Query("userId") String userId);

    @POST("isPasswordMatch")
    Observable<Response<ResponseBody>> isPasswordMatch(@Header("authorization") String accessToken,
                                                       @Header("lan") String language,
                                                       @Body Map<String, Object> body);

    @POST("withdraw/money")
    Observable<Response<WithdrawSuccess>> withdraw(@Header("authorization") String accessToken,
                                                   @Header("lan") String language,
                                                   @Body Map<String, Object> body);

    @GET("withdraw")
    Observable<Response<WithdrawLog>> withdrawLogs(@Header("authorization") String accessToken,
                                                   @Header("lan") String language,
                                                   @Query("userId") String userId,
                                                   @Query("userType") String userType,
                                                   @Query("pageState") String pageState,
                                                   @Query("fetchSize") Integer fetchSize);

    @GET("connectAccount")
    Observable<Response<StripeResponse>> getStripeAccount(@Header("authorization") String accessToken,
                                                          @Header("lan") String language);

    @POST("connectAccount")
    Observable<Response<Error>> addStripAccount(@Header("authorization") String accessToken,
                                                @Header("lan") String language,
                                                @Body Map<String, Object> body);

    @POST("wallet/recharge")
    Observable<Response<RechargeResponse>> recharge(@Header("authorization") String accessToken,
                                                    @Header("lan") String language,
                                                    @Body Map<String, Object> body);

    @POST("externalAccount")
    Observable<Response<Error>> addBank(@Header("authorization") String accessToken,
                                        @Header("lan") String language,
                                        @Body Map<String, String> body);

    @GET("connectAccountCountry")
    Observable<Response<CountryResponse>> getStripeCountry(@Header("authorization") String accessToken,
                                                           @Header("lan") String language);

    @GET("wallet/check")
    Observable<Response<ResponseBody>> checkWalletAvailability(@Header("authorization") String accessToken,
                                                               @Header("lan") String language,
                                                               @Query("userId") String userId,
                                                               @Query("userName") String userName,
                                                               @Query("userType") String userType);

    @HTTP(method = "DELETE", path = "externalAccount", hasBody = true)
    Observable<Response<Error>> deleteBankAccount(@Header("authorization") String auth,
                                                  @Header("lan") String lang,
                                                  @Body Map<String, Object> body);

    @GET("country/amounts")
    Observable<Response<SuggestedAmountResponse>> suggestedAmount(@Header("authorization") String auth,
                                                                  @Header("lan") String lang,
                                                                  @Query("country_name") String country);

    @GET("withdraw/details")
    Observable<Response<WithdawStatusResponse>> withdawDetail(@Header("authorization") String auth,
                                                              @Header("lan") String lang,
                                                              @Query("withdrawId") String withdawId);

    @GET("calls")
    Observable<Response<PendingCallsResponse>> getPendingCalls(@Header("authorization") String auth,
                                                               @Header("lan") String lang);

    @PATCH("latLong")
    Observable<Response<ResponseBody>> patchLatLong(@Header("authorization") String auth,
                                                    @Header("lan") String lang,
                                                    @Body Map<String, Object> map);

    @POST("login/social")
    Observable<Response<Login>> socialSignIn(@Header("authorization") String things,
                                             @Header("language") String language,
                                             @Body Map<String, String> map);

    // COIN APIS START //

    /**
     * Get list of coin plans
     *
     * @param apiToken : token
     * @param language : language of response
     * @param offset   :  page
     * @param limit    : data limit in page
     * @return : list of data
     */
    @GET("coinPlans")
    Observable<Response<CoinPackageResponse>> coinPlans(@Header("authorization") String apiToken,
                                                        @Header("lan") String language,
                                                        @Query("offset") int offset,
                                                        @Query("limit") int limit);

    /**
     * Transfer coin
     *
     * @param apiToken : token
     * @param language : language of response
     * @param params   : required parameters
     * @return : response
     */
    @POST("tipAmount")
    Observable<Response<TransferResponse>> transferCoin(@Header("authorization") String apiToken,
                                                        @Header("lan") String language,
                                                        @Body Map<String, Object> params);
    // COIN APIS END //


    @DELETE("livestream")
    Observable<Response<ResponseBody>> deleteLiveStream(@Header("authorization") String apiToken,
                                                        @Header("lan") String language,
                                                        @Query("streamId") String streamId);

    @GET("cognitoToken")
    Observable<Response<CongnitoResponse>> onTOGetCognitoToken(@Header("authorization") String authorization,
                                                               @Header("lang") String lang);


    /**
     * buy coin
     *
     * @param apiToken : token
     * @param language : language of response
     * @param params   : required parameters
     * @return : response
     */
//    @POST("buyCoins")
    @POST("purchaseCoins")
    Observable<Response<ResponseBody>> acknowledgePurchase(@Header("authorization") String apiToken,
                                                           @Header("lan") String language,
                                                           @Body Map<String, Object> params);

    /**
     * giftTransfer
     *
     * @param apiToken : token
     * @param language : language of response
     * @param params   : required parameters
     * @return : response
     */
    @POST("giftTransfer")
    Observable<Response<GiftResponse>> sendGift(@Header("authorization") String apiToken,
                                                @Header("lan") String language,
                                                @Body Map<String, Object> params);


    /**
     * get gifts with categories
     *
     * @param apiToken : token
     * @param language : language of response
     * @return : response
     */
    @GET("giftsWithCategories")
    Observable<Response<AllGiftsResponse>> getGifts(@Header("authorization") String apiToken,
                                                    @Header("lan") String language);

    /**
     * For isometrik groupstreaming
     */
//    @GET("follow/all")
//    Observable<Response<MultiLiveUsersModel>> fetchGsUsers(@Header("authorization") String auth,
//                                                           @Header("lang") String lang, @Query("skip") int skip, @Query("limit") int limit);
//
//    @GET("follow/all/search")
//    Observable<Response<MultiLiveUsersModel>> searchGsUsers(@Header("authorization") String auth,
//                                                            @Header("lang") String lang, @Query("skip") int skip, @Query("limit") int limit,
//                                                            @Query("searchText") String searchText);

    @POST("guestToken")
    Observable<Response<GuestTokenResponse>> getGuestToken(@Header("authorization") String auth,
                                                           @Header("lang") String lang,
                                                           @Body Map<String, Object> params);

    @DELETE("call")
    Call<ResponseBody> rejectCall(@Header("authorization") String auth,
                                  @Header("lan") String lang,
                                  @Query("callId") String callId,
                                  @Query("callFrom") String from);

    @GET("isExists/emailId")
    Observable<Response<ResponseBody>> verifyIsEmailRegistered(@Header("authorization") String auth,
                                                               @Query("emailId") String email,
                                                               @Header("lang") String lang);

    @GET("isExists/userName")
    Observable<Response<ResponseBody>> verifyIsUserNameRegistered(@Header("authorization") String auth,
                                                                  @Query("userName") String userName,
                                                                  @Header("lang") String lang);

    @GET("collection")
    Observable<Response<CollectionPostsResponse>> getCollectionPostsById(@Header("authorization") String apiToken,
                                                                         @Header("lang") String language,
                                                                         @Query("collectionId") String collectionId,
                                                                         @Query("offset") int offset,
                                                                         @Query("limit") int limit);

    @POST("starUsers/subscribe")
    Observable<Response<ResponseBody>> subscribeStarUSer(@Header("authorization") String apiToken,
                                                         @Header("lan") String language,
                                                         @Body Map<String, Object> params);

    @POST("starUsers/unSubscribe")
    Observable<Response<ResponseBody>> unsubscribeStarUSer(@Header("authorization") String apiToken,
                                                           @Header("lan") String language,
                                                           @Body Map<String, Object> params);

    @GET("buyPost")
    Observable<Response<ProfilePostRequest>> getPurchasePost(@Header("authorization") String auth,
                                                             @Header("lang") String lang,
                                                             @Query("skip") int skip,
                                                             @Query("limit") int limit);

    @GET("starUsers/paidPost")
    Observable<Response<ProfilePostRequest>> getPaidPost(@Header("authorization") String auth,
                                                         @Header("lang") String lang,
                                                         @Query("memberId") String memberId,
                                                         @Query("skip") int skip,
                                                         @Query("limit") int limit);

    @POST("buyPost")
    Observable<Response<ResponseBody>> buyPost(@Header("authorization") String apiToken,
                                               @Header("lan") String language,
                                               @Body Map<String, Object> params);

    @GET("starUsers/subscribe")
    Observable<Response<SubscriptionListRes>> getSubscriptions(@Header("authorization") String apiToken,
                                                               @Header("lang") String language,
                                                               @Query("status") int status,
                                                               @Query("offset") int offset,
                                                               @Query("limit") int limit);

    @POST("subscription")
    Observable<Response<ResponseBody>> postSubscription(@Header("authorization") String apiToken,
                                                        @Header("lan") String language,
                                                        @Body Map<String, Object> params);

    @POST("business/emailPhoneExists")
    Observable<Response<Error>> emailPhoneVerification(@Header("authorization") String apiToken,
                                                       @Header("lang") String language,
                                                       @Body Map<String, Object> map);

    @POST("business/emailPhoneExists")
    Call<Error> emailPhoneVerification1(@Header("authorization") String apiToken,
                                        @Header("lang") String language,
                                        @Body Map<String, Object> map);

    @POST("forgotPassword")
    Observable<Response<Error>> forgotPassword(@Header("authorization") String auth,
                                               @Header("lang") String lang,
                                               @Body Map<String, Object> map);

    @POST("business/emailPhoneSend")
    Observable<Response<Error>> sendVerification(@Header("authorization") String auth,
                                                 @Header("lang") String lang,
                                                 @Body Map<String, Object> map);

    @GET("withdraw/withdrawAmt")
    Observable<Response<WithdrawAmountCheckResponse>> withdrawAmount(@Header("authorization") String apiToken,
                                                                     @Header("lan") String language,
                                                                     @Query("walletId") String walletId,
                                                                     @Query("withdrawCurrency") String withdrawCurrency,
                                                                     @Query("type") String type,
                                                                     @Query("amount") String amount);

    @POST("transfer/validate")
    Observable<Response<Error>> validateTransfer(@Header("authorization") String apiToken,
                                                 @Header("lan") String language,
                                                 @Body Map<String, Object> params);

    @GET("user/transfer/detail")
    Call<TranResponse> transferDetail(@Header("authorization") String auth,
                                      @Header("lan") String lang,
                                      @Query("transactionId") String callId);


    @GET("country/link")
    Observable<Response<WithdrawMethodResponse>> paymentMethods(@Header("authorization") String accessToken,
                                                                @Header("lan") String language,
                                                                @Query("countryCode") String countryCode,
                                                                @Query("offset") int offset,
                                                                @Query("limit") int limit);

    @POST("user/transfer")
    Observable<Response<TransferResponse>> transfer(@Header("authorization") String apiToken,
                                                    @Header("lang") String language,
                                                    @Body Map<String, Object> map);

    @GET("coinAmount")
    Observable<Response<CoinAmountResponse>> getCoinAmount(@Header("authorization") String apiToken,
                                                           @Header("lan") String language);

    /**
     * get user subscribers list
     *
     * @param apiToken : token
     * @param language : language of response
     * @param status   : default 2 fixed
     * @param skip     : next page
     * @param limit    : page size
     * @return : response
     */
    @GET("starUsers/subscriberList")
    Observable<Response<FollowersResponse>> getSubscribers(@Header("authorization") String apiToken,
                                                           @Header("lan") String language,
                                                           @Query("status") int status,
                                                           @Query("offset") int skip,
                                                           @Query("limit") int limit);

    @POST("profile/emailPhoneSend")
    Observable<Response<Error>> profileEmailPhoneVerification(@Header("authorization") String apiToken,
                                                              @Header("lang") String language,
                                                              @Body Map<String, Object> map);

    @DELETE("profile/users")
    Observable<Response<ResponseBody>> deleteAccount(@Header("authorization") String apiToken);

    @GET("profile/member")
    Call<Profile> checkUser(@Header("authorization") String apiToken,
                            @Header("lang") String language,
                            @Query("memberId") String memberId);



    @GET("history")
    Observable<Response<CallHistoryResponse>> getUserCallHistory(@Header("authorization") String auth,
                                                                 @Header("lan") String lang,
                                                                 @Query("userId") String userId,
                                                                 @Query("skip") int skip,
                                                                 @Query("limit") int limit);
}
