package com.example

import org.junit.runner.RunWith
import org.specs2.Specification
import org.specs2.runner.JUnitRunner

@RunWith( classOf[ JUnitRunner ] )
class PeopleServiceSpecification extends JaxRsSpecification { def is =
    "Create new person with email <a@b.com>"                                                      ^ br^
    	"Given REST client for application deployed at ${http://localhost:8080}"                  ^ client^
        "When I do POST to ${rest/api/people}"                                                    ^ post(
        	Map(
                "email" -> "a@b.com", 
                "firstName" -> "Tommy", 
                "lastName" -> "Knocker"
            )
        )^
        "Then I expect HTTP code ${201}"                                                          ^ expectResponseCode^
        "And HTTP header ${Location} to contain ${http://localhost:8080/rest/api/people/a@b.com}" ^ expectResponseHeader^
    endp^
    "Retrieve existing person with email <a@b.com>"                                               ^ br^
    	"Given REST client for application deployed at ${http://localhost:8080}"                  ^ client^
        "When I do GET to ${rest/api/people/a@b.com}"                                             ^ get^
        "Then I expect HTTP code ${200}"                                                          ^ expectResponseCode^
        "And content to contain ${JSON}"                                                          ^ expectResponseContent(
            """
                {
                    "email": "a@b.com", 
                    "firstName": "Tommy", 
                    "lastName": "Knocker" 
                }            
            """
         )^
    endp^
    "Retrieve all people"                                                                         ^ br^
    	"Given REST client for application deployed at ${http://localhost:8080}"                  ^ client^
        "When I do GET to ${rest/api/people}"                                                     ^ get^
        "Then I expect HTTP code ${200}"                                                          ^ expectResponseCode^
        "And content to contain ${JSON}"                                                          ^ expectResponseContent(
            """
            [
                {
                    "email": "a@b.com", 
                    "firstName": "Tommy", 
                    "lastName": "Knocker"
                }
            ]
            """    
         )^
    endp^
    "Modify firts name of existing person with email <a@b.com>"                                   ^ br^
    	"Given REST client for application deployed at ${http://localhost:8080}"                  ^ client^
        "When I do PUT to ${rest/api/people/a@b.com}"                                             ^ put(
        	Map( 
                "firstName" -> "Tom" 
            )
        )^
        "Then I expect HTTP code ${200}"                                                          ^ expectResponseCode^
        "And content to contain ${JSON}"                                                          ^ expectResponseContent(
            """
                {
                    "email": "a@b.com", 
                    "firstName": "Tom",
                    "lastName": "Knocker" 
                }
            """
         )^
    endp^
    "Create yet another person with email <a@b.com>"                                              ^ br^
    	"Given REST client for application deployed at ${http://localhost:8080}"                  ^ client^
        "When I do POST to ${rest/api/people}"                                                    ^ post(
        	Map( 
                "email" -> "a@b.com" 
            )
        )^
        "Then I expect HTTP code ${409}"                                                          ^ expectResponseCode^
        "And content to contain ${Person already exists: a@b.com}"                                ^ expectResponseContent^
    endp^    
    "Delete non-existing person with email <b@b.com>"                                             ^ br^
    	"Given REST client for application deployed at ${http://localhost:8080}"                  ^ client^
        "When I do DELETE to ${rest/api/people/b@b.com}"                                          ^ delete^ 
        "Then I expect HTTP code ${404}"                                                          ^ expectResponseCode^
        "And content to contain ${Person not found: b@b.com}"                                     ^ expectResponseContent^
    endp^    
    "Delete existing person with email <a@b.com>"                                                 ^ br^
    	"Given REST client for application deployed at ${http://localhost:8080}"                  ^ client^
        "When I do DELETE to ${rest/api/people/a@b.com}"                                          ^ delete^
        "Then I expect HTTP code ${200}"                                                          ^ expectResponseCode^
    endp^
    end
}