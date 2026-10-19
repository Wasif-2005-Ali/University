INCLUDE Irvine32.inc

.data
         arr DWORD 1,2,3,4,5

.code
main PROC

         mov  esi, OFFSET arr
         mov  edi, OFFSET arr + SIZEOF arr - TYPE arr
         mov  ecx, LENGTHOF arr / 2

    L1:
         mov  eax, [esi]
         mov  ebx, [edi]

         mov  [esi], ebx
         mov  [edi], eax

         add  esi, TYPE arr
         sub  edi, TYPE arr
         loop L1

         mov  esi, OFFSET arr
         mov  ecx, LENGTHOF arr
         mov  ebx, TYPE arr

         call DumpMem

         exit
main ENDP
END main