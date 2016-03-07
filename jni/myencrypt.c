#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include "format.h"
#include "ibe_progs.h"
#include <errno.h>

CONF_CTX *cnfctx;

params_t params;



void myencrypt()
{
    FILE *fpin, *fpou;

    char defaultcnffile[] = "/sdcard/Android/data/com.instil.ibeattest/files/ibe.cnf";


    char **idarray;
    int i;
    int count;
    char *argv[2];
    argv[1] = "har";
    
    char *cnffile = defaultcnffile;
    char *paramsfile;
    int status;


    cnfctx = LoadConfig(cnffile);

    if (!cnfctx) {
	fprintf(stderr, "error opening %s\n", cnffile);
	exit(1);
    }
    
    paramsfile = GetPathParam(cnfctx, "params", 0, "/sdcard/Android/data/com.instil.ibeattest/files/params.txt");


    IBE_init();
    status = FMT_load_params(params, paramsfile);
    if (status != 1) {
	fprintf(stderr, "error loading params file %s\n", paramsfile);
	exit(1);
    }


    count = 1;
    idarray = (char **) alloca(sizeof(char *) * count);

    for (i=0; i<count; i++) {
    idarray[i] = FMT_make_id(argv[i + 1], NULL, params);
    }

    if ((fpin = fopen ("/sdcard/Android/data/com.instil.ibeattest/files/plain.txt", "r")) == NULL) {
        fprintf (stderr,"Error %d opening 'plain.txt'\n", errno);
    }
	
    if ((fpou = fopen ("/sdcard/Android/data/com.instil.ibeattest/files/cipher.txt", "w")) == NULL) {
        fprintf (stderr,"Error %d opening 'cipher.txt'\n", errno);
    }
    
    FMT_encrypt_stream_array(idarray, 1 , fpin, fpou, params);

    fclose(fpou);
    fclose(fpin);

    for (i=0; i<count; i++) {
 
    free(idarray[i]);
    }

    params_clear(params);

    IBE_clear();
}

