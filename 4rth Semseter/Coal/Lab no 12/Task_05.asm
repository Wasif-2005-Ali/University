INCLUDE Irvine32.inc

.data
         plaintext BYTE "HELLO WORLD",0
         key       BYTE "ABXmv#7",0

         buffer    BYTE SIZEOF plaintext DUP(0)

         plainMsg  BYTE "Original: ",0
         encMsg    BYTE "Encrypted: ",0
         decMsg    BYTE "Decrypted: ",0

.code
main PROC

                   ; Display original text
                   mov  edx, OFFSET plainMsg
                   call WriteString

                   mov  edx, OFFSET plaintext
                   call WriteString
                   call Crlf

                   ; Encrypt
                   mov  esi, OFFSET plaintext
                   mov  edi, OFFSET buffer
                   mov  ebx, OFFSET key

    EncryptLoop:

                   mov  al, [esi]
                   cmp  al, 0
                   je   ShowEncrypted

                   mov  dl, [ebx]

                   xor  al, dl
                   mov  [edi], al

                   inc  esi
                   inc  edi
                   inc  ebx

                   ; Repeat key if end reached
                   cmp  BYTE PTR [ebx], 0
                   jne  EncryptLoop

                   mov  ebx, OFFSET key
                   jmp  EncryptLoop

    ShowEncrypted:

                   mov  BYTE PTR [edi], 0

                   mov  edx, OFFSET encMsg
                   call WriteString

                   mov  edx, OFFSET buffer
                   call WriteString
                   call Crlf

                   ; Decrypt (XOR again)
                   mov  esi, OFFSET buffer
                   mov  ebx, OFFSET key

    DecryptLoop:

                   mov  al, [esi]
                   cmp  al, 0
                   je   Done

                   mov  dl, [ebx]

                   xor  al, dl
                   call WriteChar

                   inc  esi
                   inc  ebx

                   cmp  BYTE PTR [ebx], 0
                   jne  DecryptLoop

                   mov  ebx, OFFSET key
                   jmp  DecryptLoop

    Done:
                   call Crlf
                   exit

main ENDP
END main