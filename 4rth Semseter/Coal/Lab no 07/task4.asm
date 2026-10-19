INCLUDE Irvine32.inc

.data
         v1   DWORD 25
         v2   DWORD 50
         v3   DWORD 60
         Rval DWORD ?

.code
main PROC

         mov  eax, v1
         neg  eax          ; -v1

         add  eax, v2      ; -v1 + v2

         mov  ebx, v3
         neg  ebx          ; -v3
         sub  ebx, v2      ; -v3 - v2

         sub  eax, ebx     ; subtract

         mov  Rval, eax

         ; display result
         call WriteInt
         call Crlf

         exit
main ENDP
END main