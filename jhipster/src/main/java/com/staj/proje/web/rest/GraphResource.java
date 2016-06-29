package com.staj.proje.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import com.staj.proje.domain.User;
import com.staj.proje.repository.UserRepository;
import com.staj.proje.security.SecurityUtils;
import com.staj.proje.service.MailService;
import com.staj.proje.service.UserService;
import com.staj.proje.web.rest.dto.KeyAndPasswordDTO;
import com.staj.proje.web.rest.dto.ManagedUserDTO;
import com.staj.proje.web.rest.dto.UserDTO;
import com.staj.proje.web.rest.util.HeaderUtil;

import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.net.URLEncoder;

import com.staj.proje.Node;
/**
 * REST controller for managing Item.
 */
@RestController
@RequestMapping("/api")
public class GraphResource {

    @RequestMapping(value="/nodes",
            method = RequestMethod.GET,
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE} )
    public ResponseEntity<String> post_json() throws URISyntaxException {
    	BufferedReader reader = null;
        String input = "";
        String inputLine = "";
        try {
            URL url = new URL("http://10.150.25.16:8080/GraphData");
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                input = inputLine;
            }

            reader.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        return new ResponseEntity<String>(input, HttpStatus.OK);
    }

    @RequestMapping(value="/movies",
            method = RequestMethod.GET,
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE} )
    public ResponseEntity<String> get_movies() throws URISyntaxException {
        BufferedReader reader = null;
        String input = "";
        String inputLine = "";
        try {
            URL url = new URL("http://10.150.25.16:8080/Movies");
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                input = inputLine;
            }

            reader.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        return new ResponseEntity<String>(input, HttpStatus.OK);        
    }

    @RequestMapping(value="/people",
            method = RequestMethod.GET,
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE} )
    public ResponseEntity<String> get_people() throws URISyntaxException {
        BufferedReader reader = null;
        String input = "";
        String inputLine = "";
        try {
            URL url = new URL("http://10.150.25.16:8080/People");
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                input = inputLine;
            }

            reader.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        return new ResponseEntity<String>(input, HttpStatus.OK);    
    }

    @RequestMapping(value="/shortestPathM2P",
            method = RequestMethod.POST,
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE} )
    public ResponseEntity<String> shortest_path_M2P_json(@RequestBody String names) throws URISyntaxException {
        BufferedReader reader = null;
        String input = "";
        String inputLine = "";
        try {
            URL url = new URL("http://10.150.25.16:8080/ShortestPathM2P?" + names);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                input = inputLine;
            }

            reader.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        return new ResponseEntity<String>(input, HttpStatus.OK);
    }

    @RequestMapping(value="/shortestPathM2M",
            method = RequestMethod.POST,
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE} )
    public ResponseEntity<String> shortest_path_M2M_json(@RequestBody String names) throws URISyntaxException {
        BufferedReader reader = null;
        String input = "";
        String inputLine = "";
        try {
            URL url = new URL("http://10.150.25.16:8080/ShortestPathM2M?" + names);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                input = inputLine;
            }

            reader.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        return new ResponseEntity<String>(input, HttpStatus.OK);
    }

    @RequestMapping(value="/shortestPathP2P",
            method = RequestMethod.POST,
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE} )
    public ResponseEntity<String> shortest_path_P2P_json(@RequestBody String names) throws URISyntaxException {
        BufferedReader reader = null;
        String input = "";
        String inputLine = "";
        try {
            URL url = new URL("http://10.150.25.16:8080/ShortestPathP2P?" + names);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                input = inputLine;
            }

            reader.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        return new ResponseEntity<String>(input, HttpStatus.OK);
    }


    @RequestMapping(value="/sendMovie",
            method = RequestMethod.POST,
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE} )
    public ResponseEntity<String> send_movie_json(@RequestBody String movie_name) throws URISyntaxException {

        BufferedReader reader = null;
        String input = "";
        String inputLine = "";
        System.out.println(movie_name);
        try {
            URL url = new URL("http://10.150.25.16:8080/MovieAnalysis?movieName=" + URLEncoder.encode(movie_name, "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                input = inputLine;
            }

            reader.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        return new ResponseEntity<String>(input, HttpStatus.OK);
    }

    @RequestMapping(value="/relations",
            method = RequestMethod.GET,
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE} )
    public ResponseEntity<String> get_relations_json() throws URISyntaxException {
        BufferedReader reader = null;
        String input = "";
        String inputLine = "";
        try {
            URL url = new URL("http://10.150.25.16:8080/Relations");
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                input = inputLine;
            }

            reader.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        return new ResponseEntity<String>(input, HttpStatus.OK);
    }

    @RequestMapping(value="/degreeCentrality",
            method = RequestMethod.POST,
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE} )
    public ResponseEntity<String> send_relation_json(@RequestBody String relation) throws URISyntaxException {
        BufferedReader reader = null;
        String input = "", type = "";
        String inputLine = "";

        String[] values = relation.split("&");
        String[] parsed_values = values[0].split("=");
        System.out.println(parsed_values[1]);
        if(parsed_values[1].equals("People")) type = "P";
        else if(parsed_values[1].equals("Movie")) type = "M";

        try {
            System.out.println(type);
            URL url = new URL("http://10.150.25.16:8080/DegreeCentrality" + type + "?" + values[1]);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                input = inputLine;
            }

            reader.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        return new ResponseEntity<String>(input, HttpStatus.OK);
    }
}