package org.knowledge4retail.api.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.customer.dto.CustomerDto;
import org.knowledge4retail.api.customer.exception.CustomerNotFoundException;
import org.knowledge4retail.api.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {


    private final CustomerService customerService;

    @Operation(
            summary = "Creates a new Customer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer successfully created", content = @Content(schema = @Schema(implementation = CustomerDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error")
            }
    )
    @PostMapping("api/v0/customers")
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customer) {

        log.debug(String.format("Received a request to create a Customer with the given details %s",
                customer.toString()));
        return new ResponseEntity<>(this.customerService.create(customer), HttpStatus.OK);

    }

    @Operation(
            summary = "Updates an existing Customer defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer successfully updated", content = @Content(schema = @Schema(implementation = CustomerDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Customer wth the given Id was not found")
            }
    )
    @PutMapping("api/v0/customers/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Integer customerId,
                                                      @Valid @RequestBody CustomerDto customer) throws CustomerNotFoundException {

        log.debug(String.format("Received a request to update the Customer with the Id %d with the given details %s",
                customerId, customer.toString()));

        customer.setId(customerId);
        checkEntityExistence(customerId);

        return new ResponseEntity<>(this.customerService.update(customerId, customer), HttpStatus.OK);

    }

    @Operation(
            summary = "Returns all Customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All Customers successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomerDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/customers")
    public ResponseEntity<List<CustomerDto>> getCustomers() {

        log.debug("Received a request to retrieve all Customers");

        return new ResponseEntity<>(this.customerService.readAll(), HttpStatus.OK);

    }

    @Operation(
            summary = "Returns customer defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = CustomerDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Customer wth the given Id was not found")
            }
    )
    @GetMapping("api/v0/customers/{customerId}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Integer customerId) throws CustomerNotFoundException {

        log.debug(String.format("Received a request to retrieve the Customer with the Id %d", customerId));
        checkEntityExistence(customerId);

        return new ResponseEntity<>(this.customerService.read(customerId), HttpStatus.OK);

    }


    @Operation(
            summary = "Deletes customer defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer with the given Id was successfully deleted", content = @Content(schema = @Schema(implementation = CustomerDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Customer wth the given Id was not found")
            }
    )
    @DeleteMapping("api/v0/customers/{customerId}")
    public ResponseEntity<Integer> deleteCustomer(@PathVariable Integer customerId) throws CustomerNotFoundException {

        log.debug(String.format("Received a request to delete Customer with the Id %d", customerId));

        checkEntityExistence(customerId);

        return new ResponseEntity<>(this.customerService.delete(customerId), HttpStatus.OK);

    }

    private void checkEntityExistence(Integer customerId) throws CustomerNotFoundException {
        if (!this.customerService.exists(customerId)) {
            log.warn(String.format("Customer with Id %d was not found", customerId));
            throw new CustomerNotFoundException();
        }
    }


}
