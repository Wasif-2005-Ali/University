INCLUDE Irvine32.inc

.data
         vals BYTE 1,3,5,7,9
         sum  DWORD 0

.code
main PROC

         mov   ecx, LENGTHOF vals     ; loop counter
         mov   esi, OFFSET vals
         mov   eax, 0                 ; accumulator

    L1:
         movzx ebx, BYTE PTR [esi]    ; load byte → extend to 32-bit
         add   eax, ebx
         inc   esi
         loop  L1

         mov   sum, eax

         ; display result
         call  WriteInt
         call  Crlf

         exit
main ENDP
END main