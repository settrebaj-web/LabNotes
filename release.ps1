# LabNotes release helper (Task 2, 11, 14)
# Usage:  .\release.ps1 "commit message"
# Reads version.properties, commits all changes, creates tag vX.Y.Z and pushes it.
param([string]$Message = "chore(release)")
$ErrorActionPreference = "Stop"
$p = Get-Content version.properties
function Get-V($k) { (($p | Select-String "^$k=").Line -split "=")[1].Trim() }
$v = "{0}.{1}.{2}" -f (Get-V "VERSION_MAJOR"), (Get-V "VERSION_MINOR"), (Get-V "VERSION_PATCH")
git add -A
git commit -m "$Message ($v)"
git tag -a "v$v" -m "LabNotes $v"
git push
git push origin "v$v"
Write-Host ""
Write-Host "Released v$v -> https://github.com/settrebaj-web/LabNotes/actions" -ForegroundColor Green
