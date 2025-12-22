# Test script to investigate date storage issue - testing without date
Write-Host "Testing timesheet submission WITHOUT date..."

$body = @{
    employeeId = 5
    hoursWorked = 8
    description = "Testing without date field"
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