package main;

//ALL CODE DESIGNED BY ME - AI TOOLS USED TO CHANGE VARIABLE NAMES, DEBUG CERTAIN BUGS, ETC.A
//This looks handy:
//https://tls13.xargs.org/#client-key-exchange-generation (Seems to use big endian bytes)
//This also looks handy:
//https://en.wikipedia.org/wiki/SHA-2#Pseudocode

/* Sample clienthello dump

    RECORD 1 - INITIATION
    Type: 16 (handshake record)
    Record version: 03 01
    Record length: 06 d2
    
    Handshake header
    Handshake message type: 01 (client hello)
    Handshake message length: 00 06 ce

    RECORD BODY
    Client version: 03 03
    32 bytes of random data: 83 be 5c 7e 16 03 01 06 f2 01 00 06 ee 03 03 db 40 87 6b 86 70 14 88 f8 bc 68 55 0f 16 75 24 fc
    Session ID: 
        Session ID Length: a5
        Session ID Bytes: cf f6 a7 d9 aa 51 86 23 a9 ff 41 83 c5 97 5e ad 84 aa a5 20 85
    
    Cipher suites:
        02 ac 1c 93 46 46 4f 15 6c a4 a3 5d c4 30 28 ec 7d 04 69 26 20 59 7e 8f 73 20 3d e2 67 f1 30 08 61 95 f1 bb 53 e9 e6 5f 83 16 b9 3e 05 3a 21 c7 29 37 00 c1 93 65 bd fb 1d bc ff 68 d0 ab 46 16 69 26 c8 57 64 00 20 2a 2f 31 23 68 71 c6 6d 22 c4 13 27 fa 6f b0 b7 c4 57 00 20 5a ea ea 5a 13 01 13 13 01 02 13 13 02 13 03 c0 2b 03 c0 c0 2b 2f c0 2c c0 c0 30 cc a9 cc a8 c0 13 c0 14 00 9c 00 9d 00 2f 00 2f 35 01 c0 00 06 2c c0 30 85 7a 7a cc a9 00 00 00 33 04 cc a8 c0 13 c0 ef 04 ed 2a 2a 00 01 00 11 ec 04 14 00 9c 00 9d 00 2f 00 35 01 00 06 65 da da 00 00 00 2b 00 07 c0 c6 29 3c 50 19 cb 68 9c 25 79 06 7a 7a fa b6 49 89 03 04 03 03 00 1b 00 03 02 00 02 00 0b 00 02 01 00 00 2d a3 00 02 01 01 00 07 12 00 00 fe 0d 00 da 00 b9 00 01 00 01 b0 79 00 20 5f ac a6 52 a5 3c 0b 20 49 a9 f6 37 cf 2c a3 b9 7f 51 64 88 c4 10 7e 4a a9 a3 6a a5 57 88 77 6c 43 0f c9 64 53 a6 86 7a 4e a9 04 ea 3d 6a 66 3c 79 6c 4c f0 32 04 b2 11 02 42 73 38 a5 b6 4f 69 1c 42 23 00 57 b0 75 da 1f 4e 24 f6 ba ba 03 65 5f 00 f1 8b ca 04 ae ac 7a 49 eb 41 a5 85 a5 a2 10 22 c4 9d 5b f7 f3 85 57 06 f5 0c 03 4a f8 33 42 e9 18 3c 0e ce cd d5 ca 65 35 58 62 ea 87 77 db f1 39 c1 a3 32 83 4a a4 17 e9 c5 87 27 25 a9 12 7a 9c 68 55 24 69 40 de 4c 40 7c a6 33 4f fc cc e6 e8 59 08 53 7d 1f 50 ac 58 16 8e 22 fd 7d 23 36 0e 55 e4 3d e0 ec 31 c5 b1 37 d5 38 8e 6d 4a ad c5 29 37 51 38 9e a8 02 66 88 eb 34 0d 1e a7 de 4b c4 5c 71 e4 ba 1c c8 0b 11 9d 1a c3 3c 2a f2 75 4e a4 c6 cb 96 b3 bd 8c db 69 7f 65 44 03 53 b6 2e 4a 0d d7 81 a3 e9 86 79 17 05 4a 8e 1a 7e 8e 19 dd 63 84 87 3b e9 00 22 18 42 dd 3a a1 77 b1 e5 21 23 d8 08 ed 67 14 57 80 f9 8c 37 94 c5 9b 05 05 69 f8 83 40 27 de 6e 96 b7 7c c7 8d de 5c 6e 18 4a ff c5 8b f7 22 53 e1 3d 65 4b 98 ce 6a f1 8c a3 24 88 b9 6c 52 98 96 9a 89 17 07 5d 57 bd c6 b2 05 f3 0d 5a 7e 05 67 32 95 be dc 52 17 3d f1 56 82 1e 01 06 2e f5 4d 9d 00 0a 00 0c 00 0a ba ba 11 ec 00 1d 00 17 00 18 ff 01 00 01 00 44 cd 00 05 00 03 02 68 32 00 17 00 00 00 23 00 00 00 0d 00 12 00 10 04 03 08 04 04 01 05 03 08 05 55 f8 08 4e 86 a4 a1 c0 1f 9d 76 31 75 b3 97 52 ca 22 b2 b2 9b 33 65 06 e6 40 79 6a 14 6a cf e0 bd 9b 62 50 6e 13 3f 5d a9 21 6c c3 5d ee fc 8c 37 c1 40 05 01 08 06 06 01 00 33 86 54 1f 31 9c b9 d6 24 17 c8 d5 6a 88 10 78 5e da cd 29 70 35 62 7a 79 42 b5 04 ef 04 ed ba ba 00 01 00 11 ec 04 c0 98 43 0a 49 60 02 42 31 14 b8 8c c0 5d 93 1d 74 4a 58 8d 0c 48 24 03 8f a2 6c 3a 4a 62 03 e9 d4 c6 30 fb 15 58 96 25 66 84 2a 97 14 b1 c7 cb a8 46 80 98 69 e7 3d 3f bc 84 e3 23 33 bd c3 67 8e 80 7d f6 0c 31 fc b7 3c 53 5c 8b f1 f6 b7 b5 81 51 ab 9a 0b 67 30 75 f9 a1 8d 4f ec 89 d4 f1 a1 d6 7c 57 d5 21 2f 91 c7 3c 25 40 ae b1 ea ba 07 38 37 a8 01 91 77 13 72 57 35 c9 e0 a3 91 14 99 24 8f bb 4c 42 cc 9d 07 cb 4c 10 e9 50 01 fc b9 74 18 2e 88 f7 4d f3 13 b8 fd e6 c6 a5 cc 1a 68 01 a9 cf 30 78 38 d8 91 55 74 5f f4 f1 3f a0 38 2e 3f 1c ba a6 10 34 8a 77 63 9c 87 c7 6d c2 65 f8 55 74 fa f7 19 34 cb 9f 23 63 52 c2 79 64 58 f6 64 a3 75 87 8a a1 28 37 a1 6c fd b7 a6 83 92 c5 44 a8 70 d2 ec 7f 0e bb ab 7b 82 59 cc 1b 3a f8 af 9f 12 bf 2f 44 84 ea 00 bc 85 07 54 0a 39 ce 51 94 1a a1 a7 60 6f 85 03 ad ba 11 d4 22 11 ef 64 92 1e d1 1c 0b 71 20 69 1c 4b c1 4c f9 1e 81 3b cf c1 52 04 00 26 7c ff 48 bf 4d c9 72 6c 8f 33 ab 1d 60 61 8e 18 32 55 02 89 c0 00 e6 9b da 8c 4c 04 55 02 b4 87 28 5f b6 54 99 d6 ce cd 15 0a f3 e7 67 d9 c1 41 47 f7 32 88 cc af c4 8b 4b c9 62 91 47 9a bc e9 03 bc 68 bd 95 86 a4 89 14 6f 27 46 10 e8 9b b7 3c 96 5d ab 68 5c 3d 72 1d e6 58 49 e1 dc 7d 67 67 42 78 16 66 98 16 48 fa d1 30 83 5a b4 46 12 3e e3 23 43 cc 10 80 de 70 5a 65 59 1c cc 27 3c 6d 9c 49 eb 3a 68 6f 11 1c af 00 c5 56 a1 ca 36 f2 20 b6 9a 69 de ab c3 92 73 44 be b6 8f 24 62 c7 fb b9 8d 9f c1 bd 08 67 db 65 9e 75 01 d0 10 92 7e 64 21 45 d7 23 4f 4f e2 8f 5c e9 70 7a 3a 07 b6 83 09 b1 98 1d 19 a6 1b 7c 6c 40 a5 7a cc 8b 07 5c 09 ba 0e 73 80 51 80 ac 61 66 b1 65 2e 40 5a 85 61 cd ab 9c 23 54 1a 0e 35 86 86 02 f8 7f 34 c3 86 f8 f4 a6 8f 37 0f 37 f2 4d 3f 05 6b a8 10 41 b1 c5 c5 a0 c1 4b 8b 60 1d 08 09 74 06 d0 69 1c e4 bb 39 4b c0 62 cb 35 a3 69 96 ec aa a9 75 32 9f de 53 52 8c 6b 63 9e 25 70 8c f4 79 e1 91 9e 0b 27 a9 88 c8 ab 49 40 5f 22 77 78 1a 4c 31 11 ec 10 66 97 ab e3 a9 54 77 06 1c 3c 5a 16 7f 4b 66 12 8a 74 7d a0 67 20 7a 02 b5 75 af bd cc ad 4a 57 bb 2d 72 4e 77 87 13 0a bb 7d e2 82 15 63 0a b9 1c 0b 8e a1 39 2d 81 6a 90 28 8b 56 ee 2a b6 d5 c0 b6 11 57 14 dc d5 4d 14 73 72 22 0b c6 db 73 af 7e 02 1e 38 b9 a8 3a f4 98 f2 6b 35 8f a8 58 3c a5 a6 ac f8 09 fb 49 0e e3 c4 96 8d 85 9b 18 d7 2d 36 c4 6f 53 e3 a1 87 e0 06 fc 2b cd c5 74 9b 59 65 15 17 94 5c 1b 86 3b 66 5a 1b 3b a6 93 86 9c c7 77 27 1d 77 eb 93 55 20 a2 be d8 85 0d 20 11 d3 51 91 dd 20 85 4f f5 6a 29 37 06 fd 63 4b c7 81 13 76 08 37 78 03 aa c9 f0 81 98 33 aa 47 c9 65 65 a7 9c 21 50 5e 43 f8 18 fb c8 2d be 22 98 0d 4c 4f 4f a0 65 67 cb 1e 1e 22 1e bf 02 61 ce a4 45 5f 6b bc b9 9a b4 7e c2 95 7f 9a 34 d6 a6 cc 7a ab 86 1b a2 6c 9c 92 84 54 12 7b 25 e2 6d ea 25 69 c0 c7 c1 40 2a 54 53 11 08 e8 5b c0 94 e1 6c ae 81 85 80 68 cf 15 b4 1d 52 ea 26 f9 e5 bb 7d 63 42 45 c9 75 c7 bc 2a 7f 93 b9 18 07 11 20 70 5e aa 65 3c b7 15 b1 88 81 be 47 fa 5c c5 4a a9 08 d8 40 ad e2 75 21 f6 ac b1 cb 27 e8 7a 28 57 0c 09 fb 82 9b 56 68 cc a3 32 4e 33 31 ab 2d 55 95 7c 48 c5 4e f9 40 f4 19 66 7e a6 b5 39 bc 2e d0 87 6a c5 19 38 1d 67 7a 08 11 c7 e9 ca 05 e8 12 6e e8 9b 68 e7 81 10 32 a3 1a c7 13 8a 17 2c 24 87 2d 78 70 5d 2a 57 25 cb cd 28 20 26 b3 70 c2 26 37 7c 2b c3 67 cc 0a f7 82 96 9d 74 9e 1d 60 c9 23 f2 86 f0 db a7 57 b9 4f 93 9b 0d 9b 39 08 c9 7b 6b fe 6a 29 af 65 13 fd f3 89 b3 99 9a cb 40 00 2f 92 30 64 81 25 86 74 70 91 bb 5e 9b 88 b9 10 fa 45 ba aa c8 e2 98 a6 b6 44 9c 71 bc ba 54 30 ae 55 0c 56 ba 16 ad 58 82 8d 1b 36 0c 6d ec c4 22 da 69 0e ca 3c 40 b8 a5 b2 ab 1f 3f 2b 83 1b 04 c7 89 b2 66 1d 50 b4 dd 13 98 66 1c 3b 60 90 07 39 91 36 66 25 b3 a6 20 9b ac ba 6b c9 00 69 f6 e9 00 59 b2 3e d9 01 78 f0 87 45 a3 04 c5 75 a1 52 3a d9 6c a1 65 8d 01 62 0f ff 55 45 50 30 13 32 a0 82 28 09 05 2f db 91 81 47 66 69 ac 3e d1 ba 90 f0 49 13 5e 85 81 da 8b a0 de 75 95 4f 4b 2a 20 d6 cc 47 13 e4 f8 45 a2 fa cc 18 d8 84 f5 ac 20 48 41 1e 0a 85 ae bb 19 0c ea a6 56 6a 62 cd c6 26 46 24 55 8b 88 29 55 74 71 a4 48 ea 34 0d 80 45 12 0c 90 50 04 47 6a f1 48 29 12 48 ab fc 46 55 c7 38 b0 61 9c 37 a5 57 49 d7 1c 9b 6a cb 0f f7 52 55 76 69 aa 9b 60 29 34 ac 7f 28 23 96 84 25 15 30 19 24 30 5a d7 23 37 c6 58 71 17 e2 4c b9 c9 b6 ba 23 00 6a d1 4b b7 37 18 50 aa 17 10 12 55 26 65 b2 b0 9c 17 b9 23 71 ec 1b 87 73 75 90 26 78 5c 79 c0 a7 c8 c4 2c 5d ab 15 44 f8 86 0b 32 a3 29 41 21 bd f4 a1 23 35 b2 60 21 3f 01 1c 9b 2e c1 7d 8a 33 9c 85 b7 93 ec 6b 12 d8 97 c7 3c 61 72 fb d9 97 43 a2 24 43 1b 03 a9 37 10 09 37 ac fc 0c c8 da e1 9f 45 47 97 c7 c3 93 f3 e2 4d 09 70 a8 0d 58 96 ef 07 ad 04 07 89 ec c9 21 e1 44 8f 31 9a 61 b8 11 60 ea 02 32 b9 21 20 ed 00 8a 84 e9 42 4d 33 60 99 ea 62 fa 89 28 1c 67 40 b8 d1 23 d8 bb 1f 52 d4 69 c6 f3 86 39 0b 3c d2 7b 97 92 49 51 c5 69 95 d1 fe 38 96 dd 76 f3 83 b6 3b cf b6 70 79 9e 7a 6d 18 d4 0d 35 d0 a9 4a d8 51 e0 e5 e0 80 11 2d 50 fa c9 91 7d 7f bc b1 f3 f6 f2 7f 11 a2 e1 13 a7 07 48 db 36 23 f3 d2 03 fb ac 87 7b ba f1 e8 68 18 94 51 00 1d 00 20 19 93 ed 7e a6 6e 0c 12 10 b8 89 d3 33 76 52 8f 74 b7 b6 f2 62 3a d7 e5 0a 9b 36 27 73 2c 89 12 00 00 00 0e 00 0c 00 00 09 6c 87 c6 40 54 7b 02 05 dc f3 00 00 80 95 c9 37 52 a6 47 20 27 54 35 84 51 5f 40 c4 5f 65 fa 2e 62 ab 6a bd 34 55 61 12 3a be 02 90 98 bc b6 13 c1 5e 90 d7 cc 6d f5 36 fb 22 09 60 92 8d 8f 26 13 75 57 72 d8 c0 7a 12 23 7d c3 d9 1f c5 10 42 a1 4c 57 28 03 1e 90 27 b9 db c5 bf 6d 9b ae 9c c9 7a 21 ca b0 b1 ea 67 92 e7 b5 6c 3b 42 2a ca 1d de 82 1e f5 23 a2 1e d0 5f 60 97 84 6f 63 61 6c 68 6f 73 74 00 2d 00 02 01 01 44 cd 00 05 00 03 02 68 32 00 10 00 0e 00 0c 02 68 32 08 68 74 74 70 2f 31 2e 31 00 05 00 05 01 00 00 00 00 ff 01 00 01 00 00 0d 00 12 00 10 04 03 08 04 04 01 05 03 08 05 05 01 08 06 06 01 00 1b 00 03 02 00 02 00 2b 00 07 06 0a 0a 03 04 03 80 30 cd 16 2c 0d 50 73 57 86 c0 53 04 37 8f 90 49 af d0 f7 86 14 18 a8 6a d1 a4 2d 74 81 10 e6 8e 0a 4b 95 76 65 b2 f8 c0 b0 0e d3 09 e1 e4 77 03 fe 0d 00 fa 00 00 01 00 01 10 00 20 1f d8 fd 95 0e a3 0e f8 e3 33 9d ee c7 58 b1 6a e2 36 8a 24 48 22 42 1c 6b 87 b1 f3 f3 0e 55 0c 00 d0 9d 9c 60 fe e5 05 66 cb fd 4f 02 00 28 ef 49 69 38 89 8a 39 3c 30 20 75 18 ba d5 a3 81 93 2b 04 5d a2 29 dd bd 88 a0 f5 92 9e f5 e4 44 63 17 7d 1f 7a 36 8e ee 56 40 62 39 18 b9 77 47 f8 62 82 4c 2a a0 b6 cb 0f 4c b5 52 e0 89 36 21 a6 c7 37 8e fb 17 80 95 e4 cb fe 18 8d b7 bd 92 28 0b 78 99 76 0f 57 19 ac f4 69 e9 1f 20 4f da f2 8d 22 37 8d 1b 87 ad d8 97 73 97 a8 a8 fa 17 11 8f 83 67 c0 bd 56 3d bd 12 a0 b6 51 ef 5f d4 7a e5 d9 b5 24 76 a7 f9 cf 14 06 7b 3e f6 81 05 4b 09 a8 d3 6d 06 2a 08 24 d7 c4 78 ce 22 22 c1 bd 12 09 2e da 72 f9 fd d3 a6 44 a7 5f 69 78 71 47 0c 6c 9a 6a 42 58 59 cf 9e 43 2c b2 dc 21 40 39 8a 93 eb 68 eb 82 81 de 82 4f 0a b7 3f b5 3a 25 82 1a c5 59 20 69 60 b3 68 d2 2c 7b c0 f6 b3 d1 05 a2 d1 8c 7d 34 d0 95 19 dc 36 cb a8 a4 87 47 8f 16 6c 5d 8a c4 75 82 1c 2f 88 c5 65 6a b8 17 88 d8 12 c4 c7 c5 24 20 59 bc 79 21 16 14 98 fd 22 ac 30 bb cd 91 26 79 2b a1 5c 63 bc a5 84 b9 38 b8 86 3f 92 a4 59 c5 44 cd 30 a2 a2 a7 ba 3f 01 45 97 c5 7a 28 d6 b2 3d f5 06 18 34 8c 80 73 58 1f 65 2c 67 d8 2c 1f 90 1c 45 96 77 54 0c ba 22 5f 11 89 62 cb 7e dd 20 e7 8a ec 45 32 24 2f 5d d2 70 e7 4c 11 59 7c cf 2d 54 7b e0 8a 6c 95 38 d7 4b cb 64 b3 f1 03 65 b7 7b 48 08 57 06 21 49 7a f8 23 b7 f9 47 43 9e 17 cd 4e 32 06 0c b3 8c 87 d1 70 f9 95 8b 03 49 53 10 05 bb 5d 3a 01 d8 23 7e 87 83 77 b7 6c a0 d3 4b 46 20 d7 a3 24 d8 95 6a 93 71 13 33 55 20 b9 28 09 90 7c 20 db 20 a6 09 a2 78 c7 6a 18 39 af cb 57 c8 16 cc 90 17 15 08 ad 97 b8 f8 eb 06 a4 91 a2 69 68 c1 7d b2 36 40 7c ce 6d f9 50 5d a0 cf 18 a1 13 74 16 25 80 5c 5a de a9 16 20 f6 63 f5 92 90 8a 00 0b 00 02 01 00 00 23 00 00 00 17 00 00 00 12 00 00 00 0a 00 0c 00 0a 2a 2a 11 ec 00 1d 00 17 00 18 ca ca 00 01 00 b0 c6 0f 84 a1 d6 5b 5b b9 c9 47 a8 7e 0a d4 74 6d ef 86 3a 17 d7 c3 51 45 1f be ea 99 a6 38 1f 73 0b 6e 53 97 b9 ba c5 56 ab 1f 79 de 06 d8 92 51 5d 45 c9 37 ad 37 e3 95 ed 35 c2 24 68 d4 a3 72 6e 26 73 f9 ce fc 4f 00 1d 00 20 36 ce 77 1d a2 22 f5 da f8 42 ca 72 ec de 84 5a 36 ae c0 61 2f ff 26 77 de 9a e2 79 a9 05 ca 18 00 05 00 05 01 00 00 00 00 00 00 00 0e 00 0c 00 00 09 6c 6f 63 61 6c 68 6f 73 74 00 10 00 0e 00 0c 02 68 32 08 68 74 74 70 2f 31 2e 31 ca ca 00 01 00
*/

import algorithms.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    //Mapping of cipher suite codes to names
    public static final HashMap<Integer, String> ciphers = new HashMap<>();
    public static final HashMap<Integer, String> extensionNames = new HashMap<>();
    public static final ArrayList<Thread> clients = new ArrayList<>();

    private final static Object logLock = new Object();
    private final static StringBuilder log = new StringBuilder();

    public static void log(String msg) {
        if (!Thread.currentThread().getName().equals("PrinterThread")) {
            return;
        }

        synchronized (logLock) {
            log.append(msg);
        }
    }

    public static String getLog() {
        if (!Thread.currentThread().getName().equals("PrinterThread")) {
            return "";
        }
        synchronized (logLock) {
            return log.toString();
        }
    }
    
    public static void main (String[] args) {
        //Mandatory cipher suites (RFC 8446, appendix B.4)

            ciphers.put((0x13 << 8) | (0x01), "TLS_AES_128_GCM_SHA256 ");
            ciphers.put((0x13 << 8) | (0x02), "TLS_AES_256_GCM_SHA384");
            ciphers.put((0x13 << 8) | (0x03), "TLS_CHACHA20_POLY1305_SHA256");
            ciphers.put((0x13 << 8) | (0x04), "TLS_AES_128_CCM_SHA256");
            ciphers.put((0x13 << 8) | (0x05), "TLS_AES_128_CCM_8_SHA256");

        //Defined extensions (9.2: Mandatory to implement extensions)
            extensionNames.put(0, "server_name");
            extensionNames.put(0x002b, "supported_versions");
            extensionNames.put(0x0033, "key_share");


        int port = args.length > 0 ? Integer.parseInt(args[0]) : 443;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port + " at LAN address " + InetAddress.getLocalHost().getHostAddress());
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                // Handle client connection in a new thread
                Thread t = new Thread(new ClientHandler(socket));

                synchronized (clients) {
                    if(clients.isEmpty()) { // First thread connected is printerthread
                        t.setName("PrinterThread");
                    }
                }
                t.start();
                clients.add(t);
            }
        } catch (IOException e) {
            System.out.println("Error in server: " + e.getMessage());
        }
    }
}

class ClientValues { //Values we can get as a server from the client
    public static ByteArrayOutputStream clientHello = new ByteArrayOutputStream();
    public static byte[] publicKey; //Client's public key

    //Server private and public keys are in the ServerValues class.

    //Derived from client hello
    public static String clientVersion;
    public static byte[] sessionId;
    public static int sessionIdLength;
    public static int cipherSuitesLength;
    public static int[] cipherSuites;
    public static byte[] randomBytes;

    public static int extensionsLength;
    public static ArrayList<Integer> extensionTypes = new ArrayList<>();
    public static ArrayList<Integer> extensionLengths = new ArrayList<>();
    public static ArrayList<byte[]> extensionData = new ArrayList<>();

    //Key storage
    public static ArrayList<Integer> keyTypes = new ArrayList<>();
    public static ArrayList<Integer> keyLengths = new ArrayList<>();
    public static ArrayList<byte[]> keyData = new ArrayList<>();
}

class ServerValues { //Server-generated values
    public static ByteArrayOutputStream serverHello = new ByteArrayOutputStream();
    public static byte[] publicKey;
    public static byte[] privateKey;
    public static byte[] sharedSecret;

    public static ByteArrayOutputStream transcript = new ByteArrayOutputStream();
    public static byte[] transcriptHash;

    public static byte[] handshake_traffic_secret;
}

class TransferringBufferedInputStream extends BufferedInputStream {
    private final ByteArrayOutputStream out;

    TransferringBufferedInputStream (InputStream in, ByteArrayOutputStream out) {
        super(in);
        this.out = out;
    }

    @Override
    public int read() throws IOException {
        int character = super.read();
        if(character == -1) {
            return -1;
        }

        this.out.write(character);

        return character;
    }
    public int read(boolean transfer) throws IOException {
        int character = super.read();
        if(character == -1) {
            return -1;
        }

        if(transfer) this.out.write(character);

        return character;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        if (bytesRead == -1) {
            return -1;
        }
        
        this.out.write(b, off, bytesRead);
        return bytesRead;
    }
    public int read(byte[] b, int off, int len, boolean transfer) throws IOException {
        int bytesRead = super.read(b, off, len);
        if (bytesRead == -1) {
            return -1;
        }
        
        if(transfer) this.out.write(b, off, bytesRead);
        return bytesRead;
    }

    public byte[] readNBytes(int len, boolean transfer) throws IOException {
        byte[] data = super.readNBytes(len);
        if(transfer && data != null) {
            this.out.write(data, 0, data.length);
        }
        return data;
    }
}
class RecordOutputStream extends ByteArrayOutputStream {
    private final int type;
    private final ByteArrayOutputStream record = new ByteArrayOutputStream();
    private final ByteArrayOutputStream message = new ByteArrayOutputStream();
    private int recordLength;
    
    RecordOutputStream(int type, boolean ciphertext) {
        this.type = type;
        this.isCiphertext = ciphertext;
        record.write(this.type);
        record.write(0x0303);
    }

    void addMessage(int type) throws IOException {
        switch(type) {
            case 0x02 -> { //ServerHello
                ServerHello(new byte[] {0x13, 0x02}); //Cipher I like
            }
            case 0x08 -> { //EncryptedExtensions
                
            }
        }

        this.recordLength += message.size();
    }
    void addMessage(int type, byte[]... params) throws IOException {
        switch(type) {
            case 0x00 -> {
            }
        }

        this.recordLength += message.size();
        ServerValues.transcript.write(message.toByteArray());
        message.flush();
    }

    void ServerHello (byte[] cipher_suite) throws IOException {
        Algorithms alg = new Algorithms();
        byte[] random = new byte[32];
        message.write(0x0303);
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(random);
        ServerValues.privateKey = random;
        ServerValues.publicKey = alg.montgomery(new BigInteger("9"), ServerValues.privateKey);

        message.write(random);

        message.write(ClientValues.sessionId);
        message.write(cipher_suite);

        message.write(0x00);
        
        addExtension(43, new byte[] {0x03,0x03});
        addExtension(51, new byte[] {0x00,0x1D}, ServerValues.publicKey);

        ServerValues.serverHello.write(message.toByteArray());
    }

    void addExtension(int type, byte[]... params) throws IOException {
        ByteArrayOutputStream extension = new ByteArrayOutputStream();
        
        switch(type) {
            case 43 -> { //Supported Versions (selected_version)
                extension.write(params[0]);
            }
            case 51 -> {
                extension.write(params[0]); //Group (x25519), key_exchange (public key)
                extension.write(params[1]);
            }
        }

        byte[] length = new byte[] {(byte)((extension.size() >> 8) & 0xFF), (byte)(extension.size() & 0xFF)};
        byte[] data = extension.toByteArray();
        extension.flush();

        extension.write(type);
        extension.write(length);
        extension.write(data);

        message.write(extension.toByteArray());
    }

    @Override
    public void flush() throws IOException {
        int length = message.size();

        record.write(recordLength);
        record.write(message.toByteArray());
        
        super.flush();

    }
}

class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private byte[] Derive_Secret(byte[] secret, byte[] label, byte[] message) throws IOException {
        Algorithms alg = new Algorithms();
        return alg.HKDF_Expand_Label(secret, label, ServerValues.transcriptHash, Algorithms.hashSize(SHA._384));
    }

    @Override
    public void run() {
        try (
            TransferringBufferedInputStream input = new TransferringBufferedInputStream(socket.getInputStream(), ClientValues.clientHello);
            BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
        ) {
            Algorithms alg = new Algorithms();
            int recordType;
            String recordVersion;
            int helloDataLength;

            int[] version = new int[2];

            //Record variables are needed, other variables will be stored in ClientValues or ServerValues
            int recordLength;
            int handshakeType;
            //String clientVersion;
            //byte[] publicKey;
            //int sessionIdLength;
            //byte[] sessionId;
            //Client Extension storage

            //Client Hello
            while(true) {
                //First byte is record type
                recordType = input.read(false);
                
                // Check for EOF (client disconnected)
                if(recordType == -1) {
                    System.out.println("Client disconnected");
                    break;
                }
                Main.log("Record type: " + recordType + "\n");

                //Get record version
                version[0] = input.read(false);
                version[1] = input.read(false);
                recordVersion = "Protocol version: TLS 1." + (version[1] - 1);
                Main.log(recordVersion + "\n");

                //Read the next two bytes to get the record length
                recordLength = input.read(false) << 8 | input.read(false);
                Main.log("Record length: " + recordLength + "\n");
                
                switch(recordType) {
                    case 0x15 -> {
                        //ALERT
                        Main.log("Received ALERT message\n");
                        int alertLevel = input.read();
                        int alertDescription = input.read();
                        Main.log("Alert Level: " + alertLevel + "\n");
                        Main.log("Alert Description: " + alertDescription + "\n");
                    }
                    case 0x16 -> {
                        //Client Hello
                        //HAND SHAKE HEADER
                        handshakeType = input.read(); //Next byte is handshake message type
                        Main.log("Handshake Message Type: "+ handshakeType + "\n");
                        helloDataLength = input.read() << 16 | input.read() << 8 | input.read() & 0xFF;

                        //Read the next two bytes to get the client version
                        version[0] = input.read();
                        version[1] = input.read();
                        ClientValues.clientVersion = "TLS 1." + (version[1] - 1);

                        Main.log("Client Version: " + ClientValues.clientVersion + "\n");

                        //Read the random 32 bytes provided by client
                        ClientValues.randomBytes = input.readNBytes(32);

                        Main.log("Client public key: " );
                        for(byte b : ClientValues.randomBytes) {
                            Main.log(String.format("%02x", b));
                        }
                        Main.log("\n");
                        //Session ID - pre-shared keys mitigate the need for this
                        //but legacy systems need a non-blank session ID to properly negotiate
                        ClientValues.sessionIdLength = (int) input.read(); //One byte representing length
                        ClientValues.sessionId = input.readNBytes(ClientValues.sessionIdLength); //Session ID bytes (not really used)

                        Main.log("Client session ID: " );
                        for(byte b : ClientValues.sessionId) {
                            Main.log(String.format("%02x", b));
                        }
                        Main.log("\n");

                        //Cipher Suites
                        ClientValues.cipherSuitesLength = input.read() << 8 | input.read(); //Two bytes representing length

                        Main.log("Cipher suites length: " + ClientValues.cipherSuitesLength + "\n");

                        //Ciphers
                        ClientValues.cipherSuites = new int[ClientValues.cipherSuitesLength/2];
                        for(int i = 0; i < ClientValues.cipherSuites.length; i++) {
                            int cipherSuiteByte = input.read() << 8 | input.read(); //Cypher suite is 2 bytes
                            Main.log("Client requests cipher suites: " + Main.ciphers.get(cipherSuiteByte) + "\n");
                        }

                        //Compression Methods (TLS 1.3 no longer supports)
                        input.readNBytes(2);

                        //Extensions Length
                        ClientValues.extensionsLength = (input.read() << 8) | (input.read() & 0xFF);
                        Main.log("Extension data total length: " + ClientValues.extensionsLength + "\n");

                        int index = 0;
                        while(index < ClientValues.extensionsLength) {
                            int extension = (input.read() << 8) | (input.read() & 0xFF);
                            ClientValues.extensionTypes.add(extension); //Extension name
                            index += 2;
                            
                            Main.log("Client Extension: " + (Main.extensionNames.get(extension) != null ? Main.extensionNames.get(extension) : String.format("%04x", extension)) + "\n");

                            //Filter GREASE values
                            if((extension >> 12) == (extension >> 4 & 0x0F) && (extension >> 8 & 0x0F) == 0x0a && (extension & 0x0F) == 0x0a) {
                                int greaseLength = (input.read() << 8) | (input.read() & 0xFF);
                                ClientValues.extensionLengths.add(greaseLength); //Extension Length
                                index+=2;
                                Main.log(" - Extension Length: " + greaseLength + "\n");

                                ClientValues.extensionData.add(input.readNBytes(greaseLength));
                                index+=greaseLength;
                                continue;
                            }

                            if(extension == 0x0033) { // Key Share Extension
                                //For some reason, there's an extra redundant two length bytes right after another two length bytes? (e.g 04 ef 04 ed 1a 1a...)
                                //We'll just use the second pair as the length instead...
                                input.readNBytes(2);
                                index+=2;
                            }

                            int extensionLength = ((input.read() & 0xFF) << 8) | (input.read() & 0xFF);
                            ClientValues.extensionLengths.add(extensionLength); //Extension Length
                            index+=2;
                            Main.log(" - Extension Length: " + extensionLength + "\n");

                            ClientValues.extensionData.add(input.readNBytes(extensionLength));
                            index+=extensionLength;
                        }

                        //Iterate through the stored extensions
                        for(Integer i : ClientValues.extensionTypes) {
                            //Main.log("Extension Name: " + Main.extensionNames.get(i));

                            switch(i) {
                                case 0x002b -> {
                                }
                                case 0x0033 -> {
                                    //Key Share

                                    int extIndex = ClientValues.extensionTypes.indexOf(i);
                                    if (extIndex == -1) {
                                        System.err.println("Extension type " + i + " not found!");
                                        return; // or skip
                                    }
                                    byte[] keyShareData = ClientValues.extensionData.get(extIndex);
                                    Main.log(" - Key Share Extension Data: ");
                                    for(byte b : keyShareData) {
                                        Main.log(String.format("%02x", b) + " ");
                                    }
                                    index = 0;
                                    int counted = 0;
                                    while(index < keyShareData.length) {// Add each key type, length, and data
                                        counted ++;
                                        int keyType = ((keyShareData[index] & 0xFF) << 8) | (keyShareData[index + 1] & 0xFF);
                                        ClientValues.keyTypes.add(keyType);
                                        index += 2;

                                        int keyLength = ((keyShareData[index] & 0xFF) << 8) | (keyShareData[index + 1] & 0xFF);
                                        ClientValues.keyLengths.add(keyLength);
                                        index += 2;

                                        if(index + keyLength > keyShareData.length) {
                                            Main.log("Key share parsing error, " + keyLength + "invalid length.\n");
                                            break;
                                        }

                                        byte[] keyData = new byte[keyLength];
                                        System.arraycopy(keyShareData, index, keyData, 0, keyLength);
                                        ClientValues.keyData.add(keyData);
                                        index += keyLength;

                                        Main.log("\nClient Key Type: " + String.format("%04x", keyType) + "\n");
                                        Main.log("Client Key Length: " + keyLength + "\n");
                                        Main.log("Client Public Key: (little-endian) ");
                                        for(byte b : keyData) {
                                            Main.log(String.format("%02x", b));
                                        }
                                        Main.log("\n");
                                        Main.log("Client Public Key: (big-endian) ");
                                        for(int j = keyLength - 1; j >= 0; j--) {
                                            Main.log(String.format("%02x", keyData[j]));
                                        }
                                        Main.log("\n");
                                    }
                                    if(ClientValues.keyTypes.contains(0x001d)) { //x25519
                                        int keyIndex = ClientValues.keyTypes.indexOf(0x001d);
                                        ClientValues.publicKey = ClientValues.keyData.get(keyIndex);
                                    } else{
                                        Main.log("Client did not offer x25519 key share, cannot proceed.\n");
                                        //Return the client an unsupported algorithm error
                                        return;
                                    }
                                    for (byte b : keyShareData) {
                                        //Main.log(String.format("%02x", b) + " ");
                                    }
                                    //Main.log("\n");
                                }
                                default -> {
                                }
                            }
                            //Main.log(" - Client supports TLS 1.3\n");
                            //Main.log(" - Extension data not parsed\n");
                        }
                        
                        //Server Hello
                        if(!ClientValues.extensionTypes.contains(0x0033)) {
                            throw new IOException("Client does not provide key_share extension");
                        }
                        SecureRandom sr = new SecureRandom();
                        ByteArrayOutputStream hello = new ByteArrayOutputStream();
                        ByteArrayOutputStream extensionData = new ByteArrayOutputStream();
                        ByteArrayOutputStream record = new ByteArrayOutputStream();
                        byte[] random32 = new byte[32];
                        
                        //SERVERHELLO
                        {
                            /*{
                            record.write(0x16);
                            record.write(new byte[] {0x03, 0x03});
                            hello.write(new byte[] {0x03, 0x03}); //Fake version so middlebox doesn't get angry
                            sr.nextBytes(random32);
                            hello.write(random32);
                            for(byte b : random32) {
                                //Just to double check
                                Main.log(String.format("%02x", b));
                            }
                            Main.log("\n");
                            Main.log("Server random bytes: ");
                            for(byte b : random32) {   
                                Main.log(String.format("%02x", b));
                            }
                            hello.write(ClientValues.sessionIdLength); //Echo client sent session id
                            Main.log("\nSession ID Length: ");
                            Main.log(String.format("%02x", ClientValues.sessionIdLength));
                            hello.write(ClientValues.sessionId);
                            Main.log("\n");
                            for(byte b : ClientValues.sessionId) {
                                Main.log(String.format("%02x", b));
                            }
                            Main.log("\n\n");

                            //Cipher suite I like
                            hello.write(new byte[] {0x13, 0x02}); //TLS_AES_256_GCM_SHA384

                            hello.write(0x00); //Null compression suite

                            //ExtensionData
                            extensionData.write(new byte[] {0x00, 0x2b, 0x00, 0x02, 0x03, 0x04}); //Supported versions - I support TLS 1.3
                            
                            //Key Share
                            Algorithms A = new Algorithms();
                            ServerValues.privateKey = new byte[32];
                            sr.nextBytes(ServerValues.privateKey);
                            ServerValues.publicKey = alg.montgomery(new BigInteger("9"), ServerValues.privateKey); //ENCRYPT THE KEY MWAHAHAHAHA
                            Main.log("Server private key: (little-endian) ");
                            for(byte b : ServerValues.privateKey) {
                                Main.log(String.format("%02x", b));
                            }
                            Main.log("\n");
                            Main.log("Server private key: (big-endian) ");
                            for(int i = 31; i >= 0; i--) {
                                Main.log(String.format("%02x", ServerValues.privateKey[i]));
                            }
                            Main.log("\n");
                            
                            //Key Share extension type
                            extensionData.write(new byte[] {
                                0x00, 0x33, //Key Share extension
                                0x00, 0x24, //Length of extension data - 36 bytes
                                0x00, 0x1d, //x25519 group
                                0x00, 0x20 //32 bytes key
                            });
                            extensionData.write(ServerValues.publicKey);
                            Main.log("Server public key: ");
                            for(byte b : ServerValues.publicKey) {
                                Main.log(String.format("%02x", b));
                            }
                            Main.log("\n");
                            
                            int extSize = extensionData.size();

                            hello.write((extSize >> 8) & 0xFF); //High byte of extensions length
                            hello.write(extSize & 0xFF); //Low byte of extensions length
                            hello.write(extensionData.toByteArray());

                            //Send
                            Main.log("The hello size: " + hello.size() + "\n");
                            int helloSize = hello.size();
                            record.write(((helloSize+4) >> 8) & 0xFF); //High byte of handshake record size
                            record.write((helloSize+4) & 0xFF); //Low byte of handshake record size
                            record.write(0x02); //Handshake type - Server Hello
                            record.write((helloSize >> 16) & 0xFF); //Highest byte of handshake size
                            record.write((helloSize >> 8) & 0xFF); //High byte of handshake size
                            record.write(helloSize & 0xFF); //Low byte of handshake record size
                            record.write(hello.toByteArray());
                            output.write(record.toByteArray());
                            output.flush();

                            ServerValues.serverHello.write(hello.toByteArray());

                            Main.log("We are now printing the server response:\n");
                            for(byte b : record.toByteArray())  {
                                Main.log(String.format("%02x", b) + " ");
                            }
                            //System.arraycopy(ClientValues.clientHello.toByteArray(), 4, temp, 0, ClientValues.clientHello.toByteArray().length-5)

                            //BigInteger point, byte[] scalar
                            ServerValues.sharedSecret = alg.montgomery(new BigInteger(1, ClientValues.publicKey), ServerValues.privateKey);

                            Main.log("\nShared secret: ");
                            for(byte b : ServerValues.sharedSecret) {
                                Main.log(String.format("%02x", b));
                            }
                            
                            
                            byte[] clientHello = ClientValues.
    clientHello.toByteArray();
                            byte[] serverHello = ServerValues.
    serverHello.toByteArray();

                            Main.log("\nClientHello bytes:\n");

                            for(byte b : clientHello) {
                                Main.log(String.format("%02x", b));
                            }
                            Main.log("\nServerHello bytes:\n");

                            for(byte b : serverHello) {
                                Main.log(String.format("%02x", b));
                            }

                            Main.log("\n\nHashed ClientHello bytes:\n");

                            //Keys calculation!!!
                            byte[] transcript = new byte[clientHello.length+serverHello.length]; //Concatenate clientHello with serverHello
                            System.arraycopy(clientHello, 0, transcript, 0, clientHello.length);
                            System.arraycopy(serverHello, 0, transcript, clientHello.length, serverHello.length);

                            ServerValues.transcriptHash = alg.SHA(SHA._384, transcript);

                            //PSK not in use, set input to 0, 0
                            byte[] zero = new byte[Algorithms.hashSize(SHA._384)];
                            byte[] early_secret = alg.HKDF_Extract(SHA._384, zero, zero);

                            byte[] derived_secret = Derive_Secret(early_secret, "derived".getBytes("US-ASCII"), "".getBytes("US-ASCII"));
                            byte[] handshake_secret = alg.HKDF_Extract(SHA._384, derived_secret, ServerValues.sharedSecret);
                            
                            ServerValues.handshake_traffic_secret = Derive_Secret(handshake_secret, "s hs traffic".getBytes("US-ASCII"), ServerValues.transcriptHash);

                            derived_secret = Derive_Secret(handshake_secret, "derived".getBytes("US-ASCII"), "".getBytes("US-ASCII"));

                            byte[] main_secret = alg.HKDF_Extract(SHA._384, derived_secret, zero);
                            }
                            */
                        }

                        //Server Hello
                        RecordOutputStream r = new RecordOutputStream(22);
                        r.addMessage(0x02);
                        r.flush();

                        //Keys calculation!!!
                        byte[] clientHello = ClientValues.
clientHello.toByteArray();
                        byte[] serverHello = ServerValues.
serverHello.toByteArray();

                        byte[] transcript = new byte[clientHello.length+serverHello.length]; //Concatenate clientHello with serverHello
                        System.arraycopy(clientHello, 0, transcript, 0, clientHello.length);
                        System.arraycopy(serverHello, 0, transcript, clientHello.length, serverHello.length);

                        //"The Hash function used by Transcript-Hash and HKDF is the cipher suite hash algorithm." - RFC 9846
                        ServerValues.transcriptHash = alg.SHA(SHA._384, transcript);

                        //PSK not in use, set input to 0, 0
                        byte[] zero = new byte[Algorithms.hashSize(SHA._384)];
                        byte[] early_secret = alg.HKDF_Extract(SHA._384, zero, zero);

                        byte[] derived_secret = Derive_Secret(early_secret, "derived".getBytes("US-ASCII"), "".getBytes("US-ASCII"));
                        byte[] handshake_secret = alg.HKDF_Extract(SHA._384, derived_secret, ServerValues.sharedSecret);
                        
                        ServerValues.handshake_traffic_secret = Derive_Secret(handshake_secret, "s hs traffic".getBytes("US-ASCII"), ServerValues.transcriptHash);

                        derived_secret = Derive_Secret(handshake_secret, "derived".getBytes("US-ASCII"), "".getBytes("US-ASCII"));

                        byte[] main_secret = alg.HKDF_Extract(SHA._384, derived_secret, zero);
                        //ENCRYPTED EXTENSIONS
                        byte[] server_write_key = alg.HKDF_Expand_Label(ServerValues.handshake_traffic_secret, random32, random32, index);
                        byte[] server_write_iv = alg.HKDF_Expand_Label(ServerValues.handshake_traffic_secret, "iv".getBytes("US-ASCII"), zero, index);
                        
                        {
                        record.write(0x16);
                        record.write(new byte[] {0x03, 0x03});
                        }

                    }
                    default -> Main.log("Unknown record type: " + recordType);
                }

                if(Thread.currentThread().getName().equals("PrinterThread")) {
                    System.out.println(Main.getLog());
                }
            }
        } catch (IOException e) {
            System.out.println("Error in client handler: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}