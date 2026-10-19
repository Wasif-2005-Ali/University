INCLUDE Irvine32.inc

.data
    regTitle DB "Student Info", 0
    regInfo  DB "Name: Wasif Ali", 0dh, 0ah, "Reg No: 245187", 0

.code
main PROC
         mov  ebx, OFFSET regTitle
         mov  edx, OFFSET regInfo
         call MsgBox
         exit
main ENDP
END main