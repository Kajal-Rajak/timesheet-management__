# Test script to investigate date storage issue
Write-Host "Testing timesheet submission with date..."

$body = @{
    employeeId = 5
    date = "2024-11-15"
    hoursWorked = 8
    description = "Testing date storage issue"
} | ConvertTo-Json

Write-Host "Request body: $body"

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/timesheets/submit" -Method POST -Headers @{"Content-Type"="application/json"} -Body $body
    Write-Host "Response: $($response.Content)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)"
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $reader.BaseStream.Position = 0
    $reader.DiscardBufferedData()
    $responseBody = $reader.ReadToEnd()
    Write-Host "Error Response: $responseBody"
}