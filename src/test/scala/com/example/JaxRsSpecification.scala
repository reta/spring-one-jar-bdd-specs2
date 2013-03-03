package com.example

import spray.json._
import DefaultJsonProtocol._
import org.glassfish.jersey.client.ClientConfig
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter
import org.specs2.Specification
import org.specs2.specification.Given
import org.specs2.specification.Given.function1ToGiven
import org.specs2.specification.Then
import org.specs2.specification.Then.function1ToThen
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.ClientRequestFilter
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response
import javax.ws.rs.core.Form
import org.specs2.specification.When
import javax.ws.rs.core.MediaType

abstract class JaxRsSpecification extends Specification {
    val client: Given[ Client ] = ( baseUrl: String ) => {
        val config = new ClientConfig()
        config.property( "baseUrl", baseUrl )
        ClientBuilder.newClient( config )
    }
       
    val clientWithAuth: Given[ Client ] = ( baseUrl: String, user: String, password: String ) => {
        val config = new ClientConfig()
        config.register( new HttpBasicAuthFilter( user, password ), classOf[ ClientRequestFilter ] )
        config.property( "baseUrl", baseUrl )
        ClientBuilder.newClient( config )
    }
    
    val expectResponseContent: Then[ Response ] = ( response: Response ) => ( content: String ) =>
        response.readEntity( classOf[ String ] ) should contain( content )
    
    val expectResponseCode: Then[ Response ] = ( response: Response ) => ( code: String ) => 
        response.getStatus() must_== code.toInt                           

    val expectResponseHeader: Then[ Response ] = ( response: Response ) => ( header: String, value: String ) =>        
        response.getHeaderString( header ) should contain( value ) 
        
    val get: When[ Client, Response ] = ( client: Client ) => ( url: String ) =>  
        client
            .target( s"${client.getConfiguration.getProperty( "baseUrl" )}/$url" )
            .request( MediaType.APPLICATION_JSON )
            .get( classOf[ Response ] )

    val delete: When[ Client, Response ] = ( client: Client ) => ( url: String ) =>  
        client
            .target( s"${client.getConfiguration.getProperty( "baseUrl" )}/$url" )
            .request( MediaType.APPLICATION_JSON )
            .delete( classOf[ Response ] )
            
    def put( values: Map[ String, Any ] ): When[ Client, Response ] = ( client: Client ) => ( url: String ) => 
        client
            .target( s"${client.getConfiguration.getProperty( "baseUrl" )}/$url" )
            .request( MediaType.APPLICATION_JSON )
            .put( 
                Entity.form( values.foldLeft( new Form() )( ( form, param ) => form.param( param._1, param._2.toString ) ) ), 
                classOf[ Response ] 
            )

   def post( values: Map[ String, Any ] ) : When[ Client, Response ] = ( client: Client ) => ( url: String ) =>  
        client
            .target( s"${client.getConfiguration.getProperty( "baseUrl" )}/$url" )
            .request( MediaType.APPLICATION_JSON )
            .post( 
                Entity.form( values.foldLeft( new Form() )( ( form, param ) => form.param( param._1, param._2.toString ) ) ),
                classOf[ Response ] 
            )
            
    def expectResponseContent( json: String ): Then[ Response ] = ( response: Response ) => ( format: String ) => {
    	format match { 
            case "JSON" => response.readEntity( classOf[ String ] ).asJson must_== json.asJson
            case _ => response.readEntity( classOf[ String ] ) must_== json
        }
    }            
}