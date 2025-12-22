# Test script to check what happens when date is not provided
Write-Host "Testing timesheet submission WITHOUT date field..."

$timesheetBody = @{
    employeeId = 5
    hoursWorked = 6
    description = "Testing without date - should this be null?"
} | ConvertTo-Json

Write-Host "Request body: $timesheetBody"

try {
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