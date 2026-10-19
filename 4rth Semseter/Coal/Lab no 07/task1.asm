INCLUDE Irvine32.inc

.data
         vals BYTE 1, 3, 5, 7, 9

.code
main PROC

               mov   esi, 0                 ; start index
               mov   edi, 4                 ; end index

    L1:
               mov   al, vals[esi]
               xchg  al, vals[edi]
               mov   vals[esi], al

               inc   esi
               dec   edi

               cmp   esi, edi
               jl    L1

               ; (Optional) display result
               mov   ecx, LENGTHOF vals
               mov   esi, OFFSET vals

    printLoop:
               movzx eax, BYTE PTR [esi]
               call  WriteInt
               call  Crlf
               inc   esi
               loop  printLoop

               exit
main ENDP

END main