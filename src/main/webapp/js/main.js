window.onload = init;

function init()
{
	var vakjes = document.getElementsByClassName( "vakje" );
	
    var i;
	for ( i=0; i<vakjes.length; i++) 
	{
	    vakjes[ i ].onclick = action;
	}
	
	document.getElementById( "reset" ).onclick = action;
	document.getElementById( "slim" ).onclick = action;
	document.getElementById( "dom" ).onclick = action;
	action();
	
}

function action( event )
{	
	var action = this.id ? this.id : "none";
	
	var request = { "session": sessionid, "action": action };
	
	var xhttp;
	
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function()
	{
	  if (xhttp.readyState == 4 && xhttp.status == 200) 
	  {
	    var grid = JSON.parse( xhttp.responseText );
		  
		for ( var key in grid ) 
		{
		  document.getElementById( key ).className = grid[ key ];			  
		} 
	  }		
	};
	
	xhttp.open( "POST", "./processaction", true );
	xhttp.setRequestHeader( "Content-type", "application/json" );
	xhttp.send( JSON.stringify( request ) );
}
