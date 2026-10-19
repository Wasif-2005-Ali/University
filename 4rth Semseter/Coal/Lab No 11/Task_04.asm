INCLUDE Irvine32.inc

.code
main PROC
         call Randomize

         call Random32
         call WriteHex
         call crlf

         mov  eax, 100
         call RandomRange
         call WriteInt

         exit
main ENDP
END main