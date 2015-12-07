package com.exquisitesolutions.opdracht1;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BoterKaasEierenSpel;
import model.Markering;
import model.Status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet plays Boter Kaas Eieren (Tic Tac Toe)
 * 
 * Request payload is JSON object with parameters user action and session id
 * 
 * Possible actions are:
 * - none: no action
 * - reset: resets the game to starting position
 * - vakje: a move request from user 
 * - slim: let computer play smart moves
 * - dom: let computer play random moves
 * 
 * session id is the regular JSESSION id
 * 
 * Response payload is JSON object mapping DOM element IDs to CSS classes 
 * 
 * @author Jelle-Frodo Huisman / 838022973
 *
 */
public class Opdracht1 extends HttpServlet 
{
  
  /**
   * instance variable as alternative to sessions maps regular JSESSION ids to BoterKaasEierenSpel
   *  
   */
  Map<String, BoterKaasEierenSpel> spellen = new HashMap<String, BoterKaasEierenSpel>();
  
  
  /** 
   * Process HTTP POST request, expects JSON payload
   * 
   * @param  req HttpServeletRequest
   * @param  resp HttpServeletResponse
   *          
   */
  @Override
  protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
  {
    
    // Parse the JSON
    String jsonRequest = convertStreamToString( req.getInputStream() );
    Type type = new TypeToken<Map<String, String>>(){}.getType();
    Map<String, String> jsonRequestObject = new Gson().fromJson( jsonRequest, type );
    
    String action = jsonRequestObject.get( "action" );
    String sessionId = jsonRequestObject.get( "session" );

    // Get spel from instance variable
    if( !spellen.containsKey( sessionId ) )
    {
      synchronized( spellen )
      { spellen.put( sessionId, new BoterKaasEierenSpel( Markering.KRUIS ) ); }
    }  
            
    // Process reset action
    if( action.matches( "reset.*" ) )
    {       
      synchronized( spellen )
      { spellen.put( sessionId, new BoterKaasEierenSpel( Markering.KRUIS ) ); }
    }  
    
    BoterKaasEierenSpel bke = spellen.get( sessionId ); 
    
    // Process slim/dom actions
    if( action.matches( "slim.*" ) ) { bke.setBedenkSlim( true ); }  
    if( action.matches( "dom.*" ) ){ bke.setBedenkSlim( false ); }
      
    // Process vakje action
    if( action.matches( "vakje.*" ) )
    {       
      int index = Integer.parseInt( action.substring( action.length() - 1 ) );      
      bke.verwerkZet( Markering.KRUIS, index );
      bke.verwerkZet( Markering.NUL, bke.bedenkZet( Markering.NUL ) );
    }  
       
    // Build response vakjes
    Map<String, String> jsonResponseObject = new HashMap<String, String>();
    
    for( int i=0; i<9; i++ )
    {
      jsonResponseObject.put( "vakje" + i, "vakje" );
      if( bke.getMarkering( i ) == Markering.KRUIS ){ jsonResponseObject.put( "vakje" + i, "vakje vakjeKruis" ); }
      if( bke.getMarkering( i ) == Markering.NUL ){ jsonResponseObject.put( "vakje" + i, "vakje vakjeNul" ); }
    }
    
    // Build response slim/dom
    if( bke.isBedenkSlim() ){ jsonResponseObject.put( "slim", "buttonselect" ); jsonResponseObject.put( "dom", "buttonunselect" ); }
    else{ jsonResponseObject.put( "slim", "buttonunselect" ); jsonResponseObject.put( "dom", "buttonselect" ); }  
    
    // Build response status
    jsonResponseObject.put( "gelijk", "hide" );
    jsonResponseObject.put( "spelerwint", "hide" );
    jsonResponseObject.put( "computerwint", "hide" );
    if( bke.getStatus() == Status.GELIJK ){ jsonResponseObject.put( "gelijk", "unhide" ); }
    if( bke.getStatus() == Status.KRUISWINT ){ jsonResponseObject.put( "spelerwint", "unhide" ); }
    if( bke.getStatus() == Status.NULWINT ){ jsonResponseObject.put( "computerwint", "unhide" ); }
    
    String jsonResponse = new Gson().toJson( jsonResponseObject );
    
    resp.setContentType( "application/json" );  
    resp.setCharacterEncoding("UTF-8"); 
    resp.getWriter().write( jsonResponse ); 
  }
 
  /** 
   * Utility method converts java.io.InputStream to String
   * 
   * @param  is java.io.InputStream
   * @return converted to String 
   *          
   */
  private static String convertStreamToString( java.io.InputStream is ) 
  {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }
   
}
