Write-Host "Undoing departments.html..."
(Get-Content "src/main/resources/templates/departments.html" -Encoding UTF8) -replace 'https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css', 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css' -replace 'bi bi-building', 'fas fa-building' -replace 'bi bi-door-open', 'fas fa-door-open' -replace 'bi bi-box', 'fas fa-cube' -replace 'bi bi-bar-chart', 'fas fa-chart-bar' -replace 'bi bi-list-task', 'fas fa-list' -replace 'bi bi-plus-lg', 'fas fa-plus' -replace 'bi bi-inbox', 'fas fa-inbox' -replace 'bi bi-pencil-square', 'fas fa-edit' -replace 'bi bi-trash', 'fas fa-trash' -replace 'bi bi-info-circle', 'fas fa-info-circle' -replace 'bi bi-qr-code', 'fas fa-barcode' -replace 'bi bi-text-left', 'fas fa-align-left' -replace 'bi bi-x-lg', 'fas fa-times' -replace 'bi bi-floppy', 'fas fa-save' | Set-Content "src/main/resources/templates/departments.html" -Encoding UTF8

Write-Host "Undoing assets.html..."
(Get-Content "src/main/resources/templates/assets.html" -Encoding UTF8) -replace 'https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css', 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css' -replace 'bi bi-building', 'fas fa-building' -replace 'bi bi-door-open', 'fas fa-door-open' -replace 'bi bi-box', 'fas fa-cube' -replace 'bi bi-bar-chart', 'fas fa-chart-bar' -replace 'bi bi-list-task', 'fas fa-list' -replace 'bi bi-plus-lg', 'fas fa-plus' -replace 'bi bi-inbox', 'fas fa-inbox' -replace 'bi bi-pencil-square', 'fas fa-edit' -replace 'bi bi-trash', 'fas fa-trash' -replace 'bi bi-info-circle', 'fas fa-info-circle' -replace 'bi bi-check-circle', 'fas fa-check-circle' -replace 'bi bi-box-seam', 'fas fa-box' -replace 'bi bi-tools', 'fas fa-wrench' -replace 'bi bi-plus-circle', 'fas fa-plus-circle' -replace 'bi bi-floppy', 'fas fa-save' | Set-Content "src/main/resources/templates/assets.html" -Encoding UTF8

$html = Get-Content "src/main/resources/templates/assets.html" -Raw -Encoding UTF8
$modalRegex = '(?s)\s*<!-- Modal Xác nh?n xóa -->.*?</div>\s*</div>\s*</div>'
$html = $html -replace $modalRegex, ''
$html | Set-Content "src/main/resources/templates/assets.html" -Encoding UTF8

Write-Host "Undoing asset.js..."
(Get-Content "src/main/resources/static/js/asset.js" -Encoding UTF8) -replace 'bi bi-plus-circle', 'fas fa-plus-circle' -replace 'bi bi-pencil-square', 'fas fa-edit' -replace 'spinner-border spinner-border-sm', 'fas fa-spinner fa-spin' -replace 'bi bi-floppy', 'fas fa-save' | Set-Content "src/main/resources/static/js/asset.js" -Encoding UTF8

$js = Get-Content "src/main/resources/static/js/asset.js" -Raw -Encoding UTF8

$js = $js -replace "this.modal = null;`r?`n\s*this.deleteModal = null;", "this.modal = null;"

$js = $js -replace "(?s)const modalElement = document.getElementById\(AssetManager.CONFIG.MODAL_ID\);`r?`n\s*if \(modalElement\) \{`r?`n\s*this.modal = new bootstrap.Modal\(modalElement\);`r?`n\s*\}`r?`n`r?`n\s*const deleteModalElement = document.getElementById\('modalConfirmDelete'\);`r?`n\s*if \(deleteModalElement\) \{`r?`n\s*this.deleteModal = new bootstrap.Modal\(deleteModalElement\);`r?`n\s*\}", "const modalElement = document.getElementById(AssetManager.CONFIG.MODAL_ID);`n    if (modalElement) {`n      this.modal = new bootstrap.Modal(modalElement);`n    }"

$js = $js -replace "(?s)\s*// Button: Confirm Delete.*?// Button: Delete Asset", "`n`n    // Button: Delete Asset"

$newStr = '$oldStr = "  handleDeleteAsset(assetId) {`n    if (!assetId) {`n      this.showError(`"Không tìm th?y ID tài s?n`");`n      return;`n    }`n`n    document.getElementById(`"deleteAssetId`").value = assetId;`n    if (this.deleteModal) {`n      this.deleteModal.show();`n    }`n  }"'

$newRegex = '(?s)\s*handleDeleteAsset\(assetId\) \{.*?this.deleteModal\.show\(\);\s*\}\s*\}'
$oldMethod = "  handleDeleteAsset(assetId) {`n    if (!assetId) {`n      this.showError(`"Không tìm th?y ID tài s?n`");`n      return;`n    }`n`n    if (!confirm(AssetManager.CONFIG.MESSAGES.DELETE_CONFIRM)) {`n      console.log(`"[DELETE] Cancelled by user`");`n      return;`n    }`n`n    console.log(``[DELETE] Deleting asset ID: `$${assetId}``);`n    this.performDelete(assetId);`n  }"

$js = $js -replace $newRegex, "`n$oldMethod"
$js = $js -replace "\s*if \(this.deleteModal\) this.deleteModal\.hide\(\);", ""

$js | Set-Content "src/main/resources/static/js/asset.js" -Encoding UTF8

Write-Host "Undoing pom.xml..."
$pom = Get-Content "pom.xml" -Raw -Encoding UTF8
$depRegex = '(?s)\s*<dependency>\s*<groupId>org\.springframework\.boot</groupId>\s*<artifactId>spring-boot-starter-validation</artifactId>\s*</dependency>'
$pom = $pom -replace $depRegex, ''
$pom | Set-Content "pom.xml" -Encoding UTF8

Write-Host "Undoing AssetDTO.java..."
$dto = Get-Content "src/main/java/com/nhom18/quanlytaisan/dto/AssetDTO.java" -Raw -Encoding UTF8
$dto = $dto -replace 'import jakarta\.validation\.constraints\.[a-zA-Z]+;`r?`n?', ''
$dto = $dto -replace '@NotBlank\(message = "[^"]+"\)\s*', ''
$dto = $dto -replace '@NotNull\(message = "[^"]+"\)\s*', ''
$dto = $dto -replace '@Min\(value = 0, message = "[^"]+"\)\s*', ''
$dto | Set-Content "src/main/java/com/nhom18/quanlytaisan/dto/AssetDTO.java" -Encoding UTF8

Write-Host "Undoing AssetController.java..."
$con = Get-Content "src/main/java/com/nhom18/quanlytaisan/controller/AssetController.java" -Raw -Encoding UTF8
$con = $con -replace 'import jakarta\.validation\.Valid;`r?`n?', ''
$con = $con -replace '@Valid ', ''
$con | Set-Content "src/main/java/com/nhom18/quanlytaisan/controller/AssetController.java" -Encoding UTF8

Write-Host "Done undoing."
