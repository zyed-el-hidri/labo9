Write-Host "=================================================" -ForegroundColor Cyan
Write-Host "      GENERATION DE 10 CLIENTS DE TEST" -ForegroundColor Cyan
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host ""

# Vérifier si l'API est accessible
Write-Host "[ETAPE 1/2] Vérification de l'API..." -ForegroundColor Yellow
try {
    $testResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/customers" -Method Get -ErrorAction Stop
    Write-Host "[OK] API accessible." -ForegroundColor Green
}
catch {
    Write-Host "[ERREUR] Impossible de se connecter à l'API sur http://localhost:8080/api/customers" -ForegroundColor Red
    Write-Host "Assurez-vous que l'application est en cours d'exécution." -ForegroundColor Red
    exit 1
}
Write-Host ""

# Générer et insérer 10 clients
Write-Host "[ETAPE 2/2] Génération de 10 clients..." -ForegroundColor Yellow
Write-Host ""

$noms = @("John", "Alice", "Bob", "Carol", "Dave", "Eva", "Frank", "Grace", "Henry", "Irene")
$domaines = @("gmail.com", "yahoo.com", "outlook.com", "company.com", "example.org", "ulaval.ca", "hotmail.com", "protonmail.com", "icloud.com", "zoho.com")
$annees = @(1980, 1985, 1990, 1992, 1988, 1995, 1982, 1991, 1987, 1993)
$mois = @("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
$jours = @("01", "05", "10", "15", "20", "25", "28")

$createdIds = @()

# Générer et insérer chaque client
for ($i = 1; $i -le 10; $i++) {
    # Extraire des données aléatoires pour chaque client
    $nom = $noms[($i - 1) % 10]
    $annee = $annees[($i - 1) % 10]
    $mois = $mois[($i - 1) % 12]
    $jour = $jours[($i - 1) % 7]
    $domaine1 = $domaines[($i - 1) % 10]
    $domaine2 = $domaines[($i + 2) % 10]

    # Construire le JSON pour le client
    $clientData = @{
        name = "$nom Smith$i"
        birthDate = "$annee-$mois-$jour"
        emails = @{
            work = @("$($nom.ToLower()).smith$i@$domaine1")
            personal = @("$($nom.ToLower()).personal$i@$domaine2")
        }
    }
    
    # Convertir en JSON
    $jsonData = $clientData | ConvertTo-Json

    # Appeler l'API pour créer le client
    Write-Host "Création du client $i : $nom Smith$i..."
    
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/customers" -Method Post -Body $jsonData -ContentType "application/json"
        Write-Host "   ID: $($response.id)" -ForegroundColor Green
        $createdIds += $response.id
    }
    catch {
        Write-Host "   [ERREUR] Échec de la création du client: $_" -ForegroundColor Red
    }
    
    Write-Host ""
}

Write-Host "Tous les clients ont été créés avec succès!" -ForegroundColor Green
Write-Host ""

# Afficher les informations pour tester
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Pour tester la liste de tous les clients:" -ForegroundColor Cyan
Write-Host "Invoke-RestMethod -Uri 'http://localhost:8080/api/customers' -Method Get" -ForegroundColor White
Write-Host "" 
Write-Host "IDs des clients créés:" -ForegroundColor Cyan
foreach ($id in $createdIds) {
    Write-Host "- $id"
}
Write-Host "==================================================" -ForegroundColor Cyan