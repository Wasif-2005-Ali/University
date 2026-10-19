; INCLUDE Irvine32.inc

; .data
; arr1 BYTE 1,2,3,4,5
; arr2 WORD 10,20,30
; arr3 DWORD 1,2
; arr4 BYTE 10 DUP(0)
; arr5 WORD 5 DUP(1)

; .code
; main PROC

;     mov eax, LENGTHOF arr1
;     call WriteInt
;     call Crlf

;     mov eax, LENGTHOF arr2
;     call WriteInt
;     call Crlf

;     mov eax, LENGTHOF arr3
;     call WriteInt
;     call Crlf

;     mov eax, LENGTHOF arr4
;     call WriteInt
;     call Crlf

;     mov eax, LENGTHOF arr5
;     call WriteInt
;     call Crlf

;     exit
; main ENDP
; END main


INCLUDE Irvine32.inc

.data
         arr BYTE 1,2,3,4,5

.code
main PROC

         mov  eax, LENGTHOF arr
         mov  ebx, TYPE arr
         mul  ebx

         call WriteInt
         call Crlf

         exit
main ENDP
END main