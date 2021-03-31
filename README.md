# flopbox-sr2

API REST flopbox

# Command CURL

- ajouter un serveur :

```bash
curl -X POST "http://localhost:8080/flopboxApp/servers/python?address=0.0.0.0&port=2121"
```

- lister les serveurs :

```bash
curl "http://localhost:8080/flopboxApp/servers/"
```
- afficher l'adresse d'un serveur:

```bash
curl "http://localhost:8080/flopboxApp/servers/python"
```

- lister les élements d'un serveur :
```
curl "http://localhost:8080/flopboxApp/servers/python/list/"
```
- telecharger un fichier
```
curl "http://localhost:8080/flopboxApp/servers/python/file/{chemin_vers_le_fichier}"
```

- supprimer un fichier 
```
curl -X DELETE "http://localhost:8080/flopboxApp/servers/python/file/{chemin_vers_le_fichier}"
```

- creer un dossier 

```
curl  -X POST "http://localhost:8080/flopboxApp/servers/python/directory/{chemin_ou_creer_le_dossier}/{nom_du_dossier}"
```

- renommer un dossier
```
curl  -X PUT "http://localhost:8080/flopboxApp/servers/python/directory/{chemin_vers_le_dossier_a_renommer}/{nom_du_dossier}?newName={nouveau_nom}"
```

- renommer un fichier
```
curl -X PUT "http://localhost:8080/flopboxApp/servers/python/file/{chemin_vers_le_fichier_a_renommer}/{nom_du_dossier}?newName={nouveau_nom}"
```

- supprimer un serveur
```
curl -X DELETE "http://localhost:8080/flopboxApp/servers/python/"
```
ici python est le nom donné a votre serveur.
