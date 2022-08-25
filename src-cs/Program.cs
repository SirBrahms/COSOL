namespace COSOL
{
    class Program
    {
        static void Main(string[] Args)
        {
            if (Args.Length == 0) 
            {
                try 
                {
                    while (true) 
                    {
                        Console.Write("> ");
                        string? Instruction;
                        if ((Instruction = Console.ReadLine()) == null)
                            return;
                        Interpret(Instruction);
                    }
                }
                catch (Exception)
                {
                    Console.WriteLine(">?");
                    Environment.Exit(0);
                }
            }
            else 
            {
                // Read file supplied in args[0]
                try 
                {
                    StreamReader Reader = new StreamReader(Args[0]);
                    string FullString;
                    FullString = Reader.ReadToEnd();

                    FullString.Replace("/n", "");
                    Interpret(FullString);
                } 
                catch (Exception) 
                {
                    Console.WriteLine("File not found!");
                    Environment.Exit(1);
                }
                
            }
        }

        static void Interpret(string FullString)
        {
            Console.WriteLine(FullString);
        }
    }
}

