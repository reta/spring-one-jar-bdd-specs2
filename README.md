spring-one-jar-bdd-specs2
==============

val clientWithAuth: Given[ Client ] = ( baseUrl: String, user: String, password: String ) => {
    val config = new ClientConfig()
    config.register( new HttpBasicAuthFilter( user, password ), classOf[ ClientRequestFilter ] )
    config.property( "baseUrl", baseUrl )
    ClientBuilder.newClient( config )
}
    