package com.vanillax.darkskiesutility.testexamples;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitchross on 12/16/13.
 */
public class Contributor {
	@SerializedName( "login" )
    public String myLogin;
	public int contributions;

	public String getMyLogin()
	{
		return myLogin;
	}
}
