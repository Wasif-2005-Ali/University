INCLUDE Irvine32.inc

.data

         myBytes BYTE 80h,66h,0A5h
.code
main PROC

         movzx ax,myBytes

         mov   bl,[myBytes+1]

         add   ax,bx

         mov   bl,[myBytes+2]

         add   ax,bx








         call  DumpRegs

         exit
main ENDP
END main    