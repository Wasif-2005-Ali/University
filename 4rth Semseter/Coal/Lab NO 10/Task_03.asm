INCLUDE Irvine32.inc

.data
         myArray BYTE 10h, 20h, 30h, 40h, 50h, 60h, 70h

.code
main PROC

                 mov  esi, OFFSET myArray
                 mov  edi, OFFSET myArray + SIZEOF myArray - TYPE myArray
                 mov  ecx, LENGTHOF myArray / 2

    ReverseLoop:

                 mov  eax, [esi]
                 mov  ebx, [edi]
                 mov  [esi], ebx
                 mov  [edi], eax

                 add  esi, TYPE myArray
                 sub  edi, TYPE myArray

                 loop ReverseLoop


                 mov  esi, OFFSET myArray
                 mov  ecx, LENGTHOF myArray
                 mov  ebx, TYPE myArray
                 call DumpMem
                 exit
main ENDP
END main