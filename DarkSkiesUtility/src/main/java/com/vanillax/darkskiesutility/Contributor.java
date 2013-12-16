package com.vanillax.darkskiesutility;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitchross on 12/16/13.
 */
public class Contributor {
	@SerializedName( "login" )
	String myLogin;
	int contributions;

	public String getMyLogin()
	{
		return myLogin;
	}
}
