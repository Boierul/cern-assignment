# TODO API Integration Tests

This directory contains Postman collection for integration testing the TODO API endpoints.

## Collection Overview

The `integration_tests.postman_collection.json` file contains automated tests for the CERN TODO application API. The collection tests all CRUD operations for todo items.

## Prerequisites

1. **Backend Application Running**: Ensure the TODO backend application is running on `http://localhost:8099`
2. **Postman**: Install Postman application
3. **Test Data**: The application should have some initial todo data loaded

## How to Run the Tests

1. **Import the Collection**:
   - Open Postman
   - Click "Import" button
   - Select "File"
   - Choose `integration_tests.postman_collection.json`

2. **Configure Environment** (if needed):
   - The tests are configured to run against `http://localhost:8099`
   - If your application runs on a different port/host, update the URLs accordingly

3. **Run the Collection**:
   - Select the imported collection
   - Click "Run" button
   - Choose "Run CERN TODO" from the runner window
   - Click "Run CERN TODO" to execute all tests

## Test Results

Each request includes automated tests that validate:
- **HTTP Status Codes**: Correct response codes for each operation
- **Response Time**: All endpoints should respond within 200ms
- **Data Structure**: Response JSON matches expected schema
- **Data Types**: Fields contain correct data types
- **Business Logic**: Priority is non-negative, task is non-empty, etc.

## Test Data Schema

Each todo item follows this structure:
```json
{
  "id": number (non-negative integer),
  "task": string (non-empty),
  "priority": number (non-negative integer)
}
```

## Troubleshooting

- **Connection Refused**: Ensure the backend application is running on the correct port
- **Test Failures**: Check that test data exists in the database
- **Response Time Issues**: Performance problems may indicate backend optimization needed
- **Schema Validation Errors**: Backend may have changed API structure

## Integration with CI/CD

This Postman collection can be integrated with Newman (Postman's command-line collection runner) for automated testing in CI/CD pipelines:

```bash
newman run integration_tests.postman_collection.json --reporters cli,json
```
