
namespace Cosol
{
    class Program
    {
        static void Main(string[] args)
        {
            Token abc = new Token(123, Test);

            Console.WriteLine(abc.Value);
            abc.OnCall();
        }

        static void Test()
        {
            Console.WriteLine("Test");
            return;
        }
    }

    # region Tokens
    
    class Token
    {
        protected object? _Value;
        protected Action? _OnCall;
    
        public virtual object Value 
        {
            get
            {
                if (_Value != null)
                    return _Value;
                else
                    throw new NullReferenceException("No Value was set");
            }
            set
            {
                {
                    _Value = (object) value;
                }
            }
        }

        public virtual Action OnCall
        {
            get
            {
                if (_OnCall != null)
                    return _OnCall;
                else
                    throw new NullReferenceException("No Action was set");
            }
            set
            {
                {
                    _OnCall = (Action) value;
                }
            }
        }

        public Token(object Val, Action OnCall)
        {
            this.Value = Val;
            this.OnCall = OnCall;
        }
    }

    

    # endregion
}

