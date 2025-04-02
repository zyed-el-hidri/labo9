@echo off
SETLOCAL EnableDelayedExpansion

echo =================================================
echo      GENERATION DE 10 CLIENTS DE TEST
echo =================================================
echo.

:: Vérifier si curl est disponible
where curl >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERREUR] curl n'est pas disponible. Veuillez l'installer ou vous assurer qu'il est dans le PATH.
    goto :error
)

:: Vérifier que l'API est accessible
echo [ETAPE 1/2] Vérification de l'API...
curl -s http://localhost:8080/api/customers > nul
if %errorlevel% neq 0 (
    echo [ERREUR] Impossible de se connecter à l'API sur http://localhost:8080/api/customers
    echo Assurez-vous que l'application est en cours d'exécution.
    goto :error
)
echo [OK] API accessible.
echo.

:: Générer et insérer 10 clients
echo [ETAPE 2/2] Génération de 10 clients...
echo.

set "noms=John Alice Bob Carol Dave Eva Frank Grace Henry Irene"
set "domaines=gmail.com yahoo.com outlook.com company.com example.org ulaval.ca hotmail.com protonmail.com icloud.com zoho.com"
set "années=1980 1985 1990 1992 1988 1995 1982 1991 1987 1993"
set "mois=01 02 03 04 05 06 07 08 09 10 11 12"
set "jours=01 05 10 15 20 25 28"

:: Générer et insérer chaque client
for /L %%i in (1,1,10) do (
    :: Extraire des données aléatoires pour chaque client
    set /a "nom_index=%%i %% 10"
    set /a "année_index=%%i %% 10"
    set /a "mois_index=%%i %% 12"
    set /a "jour_index=%%i %% 7"
    set /a "domaine1_index=%%i %% 10"
    set /a "domaine2_index=(%%i+3) %% 10"

    :: Obtenir les valeurs à partir des indices
    call :GetArrayValue !nom_index! noms nom
    call :GetArrayValue !année_index! années année
    call :GetArrayValue !mois_index! mois mois
    call :GetArrayValue !jour_index! jours jour
    call :GetArrayValue !domaine1_index! domaines domaine1
    call :GetArrayValue !domaine2_index! domaines domaine2

    :: Construire le JSON pour le client
    set "jsonData={\"name\":\"!nom! Smith%%i\",\"birthDate\":\"!année!-!mois!-!jour!\",\"emails\":{\"work\":[\"!nom!.smith%%i@!domaine1!\"],\"personal\":[\"!nom!.personal%%i@!domaine2!\"]}}"
    
    :: Appeler l'API pour créer le client
    echo Création du client %%i: !nom! Smith%%i...
    curl -s -X POST -H "Content-Type: application/json" -d "!jsonData!" http://localhost:8080/api/customers > created_client%%i.json
    
    :: Extraire l'ID du client créé (cette ligne utilise un utilitaire externe `jq` si disponible)
    where jq >nul 2>nul
    if !errorlevel! equ 0 (
        for /f "tokens=*" %%a in ('type created_client%%i.json ^| jq -r ".id"') do set "client_id=%%a"
        echo    ID: !client_id!
    ) else (
        echo    Client créé. Voir created_client%%i.json pour les détails.
    )
    
    echo.
)

echo Tous les clients ont été créés avec succès!
echo Les détails de chaque client sont disponibles dans les fichiers created_client*.json

goto :end

:: Fonction pour obtenir une valeur d'un tableau de chaînes
:GetArrayValue
set /a index=%1
set array_name=%2
set result_var=%3

set counter=0
for %%a in (!%array_name%!) do (
    if !counter! equ !index! (
        set "%result_var%=%%a"
        exit /b
    )
    set /a counter+=1
)
exit /b

:error
echo.
echo La génération des clients a été interrompue en raison d'erreurs.
exit /b 1

:end
echo.
echo =================================================
echo Pour tester la liste de tous les clients:
echo curl http://localhost:8080/api/customers
echo =================================================
echo.
ENDLOCAL