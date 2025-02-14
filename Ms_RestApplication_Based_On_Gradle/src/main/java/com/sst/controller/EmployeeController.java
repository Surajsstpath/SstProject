package com.sst.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sst.model.Employee;
import com.sst.service.IService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
//Developing the RestController class 
@RestController
@RequestMapping("/employee")
public class EmployeeController 
{
	
	//target to dependent class 
      @Autowired
	  private IService service;
      
      @Operation(summary = "Creat new user ",description = "Insert the new employee in the data based ")
      @ApiResponse(responseCode ="201",description = "Succesfully created user")
      //Implementing the API for the Register the new Employee 
      @PostMapping("/insert")
	  public ResponseEntity<?>registerNewEmployee(@RequestBody Employee employee)
	  {
		  
		  
		  try
		  {
			  //Invoked the BLC Method 
			    String registerEmployee = service.registerEmploye(employee);
			    
			  return new ResponseEntity<String>(registerEmployee,HttpStatus.CREATED);
			  
		  }
		  catch (Exception e)
		  {
			
			e.printStackTrace();
			return new ResponseEntity<String>("Please Cheak Method Implementation Carefully ",HttpStatus.INTERNAL_SERVER_ERROR);
			  
			  
		  }//end try catch 
		  
		  
	  }//end register method 
	
	
      
      @Operation(summary = "Employee Details ",description = "fetched All Employee By Id  ")
      @ApiResponse(responseCode = "200",description = "Successfully retrived the data ")
      //Implement the method for rendering all the data 
      @GetMapping("/all")
      public ResponseEntity<?>employeeData()
      {
    	  try
    	  {
    		  List<Employee> fetchAllEmployee = service.fetchAllEmployee();
    		  return new ResponseEntity<List<Employee>>(fetchAllEmployee,HttpStatus.ACCEPTED);
    		  
    	  }
    	  catch (Exception e)
    	  {
			 e.printStackTrace();
			 return new ResponseEntity<String>("Please Cheak The Method Implementation ",HttpStatus.INTERNAL_SERVER_ERROR);
		  }
      }
      
       
      
      
      //Implementing the Find Employee Based On The Id API
      @Operation(summary = "Find by id ",description = "Search Employee By id ")
      @GetMapping("/find/{id}")
      public ResponseEntity<String> findEmployeeById(@PathVariable("id") Integer id) 
      {
    	  
         // logger.info("Fetching employee with ID: {}", id);
          
    	 
          String employee = service.findEmployeeById(id);
          
          if (employee == null)
          {
             // logger.error("Employee not found with ID: {}", id);
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
          else if (id<0)
          {
        	  return new ResponseEntity<>("Please Enter The Positive Number ",HttpStatus.NOT_FOUND);
          }
        //  logger.info("Employee found: {}", employee);
          return ResponseEntity.ok(employee);
         }
         
      
      @Operation(summary ="By  Name",description ="Find the Details by name")
      @GetMapping("/search/{name}")
      public ResponseEntity<String> findEmployeeByName(@PathVariable("name") String name)
      {
          String result = service.findByName(name);

          // Check if the result is an error message or a list of names
          if (result.startsWith("No employees found")) 
          {
              return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); // Return 404 if no employees found
          }

          return new ResponseEntity<>(result, HttpStatus.OK);  // Return 200 with names
      }
      
      
      
      
      
      
      
      //Develop the Rest API for the Delete by Id 
      @Operation(summary ="Delete Employee",description ="Delete The Employee by Id ")
      @DeleteMapping("/delete/{id}")
      public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") Integer id) 
      {
          try
          {
              // Delete the employee by ID and get the result message
              String deleteById = service.deleteById(id);
              
              // If deletion was successful, return a response with HTTP 200 (OK)
              return new ResponseEntity<>(deleteById, HttpStatus.OK);
          }
          catch (Exception e)
          {
              // In case of any exception, return a bad request with an error message
              return new ResponseEntity<>("Error deleting employee: " + e.getMessage(), HttpStatus.BAD_REQUEST);
          }
      }

      
      //Implementing the API for find Id in the form range 
      @GetMapping("/find/{startId}/{endId}")
      @Operation(summary ="Ids By Range ",description="Find The Employees By Id Range ")
      public ResponseEntity<List<Employee>> findTheRangeOfTheId(@PathVariable("startId") Long startId, @PathVariable("endId") Long endId) 
      {
          // Fetch the list of employees by ID range using the service
          List<Employee> employeesByIdRange = service.findOrdersByIdRange(startId, endId);

          // Return the list wrapped in a ResponseEntity with ACCEPTED status
           return new ResponseEntity<>(employeesByIdRange, HttpStatus.ACCEPTED);
      }
      
      
      
      
      
      
      //Implementing the update the Employee based on the Id 
      @Operation(summary ="Update Employee ",description ="Update The Employee by id")
      @PutMapping("/update")
      public ResponseEntity<String> updateExistingEmployeeDetails(@RequestBody Employee employee)
      {
          // Check if the provided employee object is valid (non-null)
          if (employee == null || employee.getIds() == null)
          {
              return new ResponseEntity<>("Employee ID is required for updating details.", HttpStatus.BAD_REQUEST);
          }

          // Call the service to update employee details
          String responseMessage = service.updateEmployeeDetailsBasedOnTheId(employee);

          // If the update is successful, return 200 OK with success message
          if (responseMessage.contains("successfully"))
          {
              return new ResponseEntity<>(responseMessage, HttpStatus.OK);
          }

          // If the employee ID is not found, return 404 Not Found with an appropriate message
          return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
      }//end of the  end point 
      
      
      
      @Operation(summary = "Balance Amount ",description ="Finding Employee B/w The Range of the Balance " )
      @GetMapping("/search/{start}/{end}")
      public ResponseEntity<List<Employee>> findEmployeesWithinBalanceRange(@PathVariable("start") Double start,@PathVariable("end") Double end)
      {
          
          // Validate input parameters
    	  
          if (start == null || end == null || start < 0 || end < 0)
          {
              return ResponseEntity.badRequest().body(null); // Invalid range
          } 

          // Fetch employees whose balance is within the specified range
          
          List<Employee> employeesWithinRange = service.findBalanceAmountBetweenTheRange(start, end);

          // Return the list of employees
          return ResponseEntity.ok(employeesWithinRange);
      }
    
	
	
}
