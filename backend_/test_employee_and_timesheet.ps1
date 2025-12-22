# Test script to create a new employee and then test timesheet submission
Write-Host "Creating a new employee..."

$employeeBody = @{
    username = "testemployee2"
    password = "password123"
    name = "Test Employee 2"
    email = "test2@example.com"
    managerId = $null
} | ConvertTo-Json

Write-Host "Employee request body: $employeeBody"

try {
    $employeeResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" -Method POST -Headers @{"Content-Type"="application/json"} -Body $employeeBody
    Write-Host "Employee created successfully: $($employeeResponse.Content)"
    
    # Extract employee ID from response (assuming it's returned)
    $employeeData = $employeeResponse.Content | ConvertFrom-Json
    $employeeId = $employeeData.employee.employeeId
    
    Write-Host "Testing timesheet submission with employee ID: $employeeId"
    
    $timesheetBody = @{
        employeeId = $employeeId
        date = "2024-11-15"
        hoursWorked = 8
        description = "Testing date storage with new employee"
    } | ConvertTo-Json
    
    $timesheetResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/timesheets/submit" -Method POST -Headers @{"Content-Type"="application/json"} -Body $timesheetBody
    Write-Host "Timesheet submitted successfully: $($timesheetResponse.Content)"
    
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)"
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $reader.BaseStream.Position = 0
    $reader.DiscardBufferedData()
    $responseBody = $reader.ReadToEnd()
    Write-Host "Error Response: $responseBody"
}