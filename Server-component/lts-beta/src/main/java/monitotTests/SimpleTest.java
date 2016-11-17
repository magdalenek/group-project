package monitotTests;

import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;

public class SimpleTest {

	public static void main (String[] args){
		String accessToken = "CAACEdEose0cBAKrjZCkFyGpTOb8pKn2WZAG9eJFZBtQYZBLQYjtAjxQdk0QRcapK8rTKn6PUaoQKXGN9FpiH1JEqzZAtWFdDTIOBZCNazas7lp0ZBixkflwEHzMIrtYSNjGacZCUYYtguZCiMyV2waNDly1nUyjeg8EAqmc2oJ5C9wBzOpFTVeAMIZArkbPjUs49ZBiG5jPcOK5jAZDZD";
		@SuppressWarnings("deprecation")
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		
		User me = fbClient.fetchObject("me", User.class);

		System.out.println(me.getName());
		System.out.println(me.getAbout());
		System.out.println("-------------Accessing posts-------------");
		Connection <Post> result = fbClient.fetchConnection("me/posts", Post.class);
		
		int postCounter=0;
loop:	for (List<Post> page : result){
			for (Post aPost : page){
				System.out.println(aPost.getMessage());
				System.out.println("Address: fb.com/"+aPost.getId());
				
				//Counters Testing:
				//System.out.println("#Likes= "+aPost.getLikesCount() + "#Comments= "+aPost.getCommentsCount()+"#Shares= "+aPost.getSharesCount());
				//System.out.println("Likes= "+aPost.getLikes().getData());
				System.out.println("-----------------------------------------");
				
				//-------------------------------------------------------------
				//Just Fetch the Last 50 Posts
				//You can eliminate this part to fetch all the posts
				postCounter++;
				if (postCounter == 50){
					break loop;
				}
				//-------------------------------------------------------------
			}
		}
		System.out.println("Total Number of Posts= "+postCounter);
		
		//-----------------------------------------second test for public pages:
		System.out.println("----------->A Page Searching ");
		Connection<Page> result_n = fbClient.fetchConnection("me/accounts", Page.class);
		for (List<Page> feedPage:result_n){
			for(Page page : feedPage){
				System.out.println(page.getName());
			}
		}
		System.out.println("-----------> End of searching");
		
	}
}
